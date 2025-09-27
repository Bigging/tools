package org.example.manager.impl;


import org.example.dao.BlackPhoneListMapper;
import org.example.dao.entity.BlackPhoneList;
import org.example.manager.BlackPhoneListManager;
import jakarta.annotation.Resource;
import org.apache.ibatis.cursor.Cursor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BlackPhoneListManagerImpl implements BlackPhoneListManager {

    @Resource
    private BlackPhoneListMapper blackPhoneListMapper;

    @Override
    @Transactional
    public Cursor<BlackPhoneList> selectByCursor(Long temporaryId) {
        return blackPhoneListMapper.selectByCursor(temporaryId);
    }

    @Override
    @Transactional
    public int updateByPrimaryKeyBatch(List<BlackPhoneList> record) {
        return blackPhoneListMapper.updateByPrimaryKeyBatch(record);
    }
}
