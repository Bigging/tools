package org.example.service.impl;

import org.example.manager.BlackPhoneListManager;
import org.example.rest.api.param.RefreshBlackPhoneListReq;
import org.example.service.IRefreshDataService;
import org.example.utils.ClassUtil;
import com.google.common.collect.Lists;
import jakarta.annotation.Resource;
import org.apache.ibatis.cursor.Cursor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.function.Consumer;
import java.util.function.Function;

@Service
public class RefreshDataServiceImpl implements IRefreshDataService {

    private static final Logger log = LoggerFactory.getLogger(RefreshDataServiceImpl.class);

    /**
     * 默认集合初始化大小
     */
    private static final int DEFAULT_INITIALCAPACITY = 11000;
    /**
     * 默认集合切分数量
     */
    private static final int DEFAULT_PARTITION_SIZE = 5000;
    /**
     * 默认每次提交sql数量
     */
    private static final int DEFAULT_SIZE = 500;

    @Resource(name = "UpdateDataThreadPool")
    private ThreadPoolExecutor updateDataThreadPool;
    @Resource
    private BlackPhoneListManager blackPhoneListManager;

    /**
     * 通用处理方法
     *
     * @param m        筛选条件
     * @param function 查询方法
     * @param str      日志关键字标识
     * @param consumer 更新方法
     */
    private <M, T> void commonHandler(M m, Function<M, Cursor<T>> function, String str, Consumer<List<T>> consumer) {
        List<T> tmpResultList = new ArrayList<>(DEFAULT_INITIALCAPACITY);
        Cursor<T> cursor = function.apply(m);
        try (cursor) {
            // 处理整数部分
            cursor.forEach(value -> {
                tmpResultList.add(value);
                if (tmpResultList.size() >= DEFAULT_PARTITION_SIZE) {
                    updateDate(tmpResultList, str, consumer, DEFAULT_SIZE);
                }
            });
            // 处理剩余的
            if (!tmpResultList.isEmpty()) {
                updateDate(tmpResultList, str, consumer, DEFAULT_SIZE);
            }
        } catch (Exception e) {
            log.error("#selectAndUpdateByCursor：更新异常", e);
        } finally {
            tmpResultList.clear();
        }
    }

    /**
     * @param tmpResultList 待执行结果集
     * @param str           能唯一标识执行位置的实体类字段
     * @param consumer      更新方法
     * @param size          每批次提交数量
     */
    private <T> void updateDate(List<T> tmpResultList, String str, Consumer<List<T>> consumer, int size) {
        log.info("当前待执行内容数量:{}", tmpResultList.size());
        List<List<T>> partition = Lists.partition(tmpResultList, size);
        Integer updateNum = partition.stream()
                .map(res -> CompletableFuture
                        .supplyAsync(() -> {
                            log.info("当前执行范围：{}-{}",
                                    ClassUtil.getField(res.getFirst(), str), ClassUtil.getField(res.getLast(), str));
                            consumer.accept(res);
                            return res.size();
                        }, updateDataThreadPool)
                        .exceptionally(err -> {
                            log.error("当前执行范围：{}-{},异常信息：{},cause{}",
                                    ClassUtil.getField(res.getFirst(), str), ClassUtil.getField(res.getLast(), str),
                                    err.getMessage(), err.getCause());
                            throw new RuntimeException("更新异常，回滚数据",err);
                        })
                )
                .toList().stream()
                .map(CompletableFuture::join)
                .reduce(0, Integer::sum);
        log.info("当前已执行内容数量:{}", updateNum);
        tmpResultList.clear();
    }

    @Override
    @Transactional
    @Async
    public void refreshBlackPhoneList(RefreshBlackPhoneListReq req) {
        Long temporaryId = null;
        if (req.temporaryId() != null) {
            temporaryId = req.temporaryId();
        }
        log.info("black_phone_list#开始执行");
        commonHandler(temporaryId, res -> blackPhoneListManager.selectByCursor(res), "getTemporaryId",
                res -> blackPhoneListManager.updateByPrimaryKeyBatch(res));
        log.info("black_phone_list#执行完成");
    }

}
