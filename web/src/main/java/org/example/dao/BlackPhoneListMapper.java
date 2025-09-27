package org.example.dao;

import org.example.dao.entity.BlackPhoneList;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.cursor.Cursor;

import java.util.List;

@Mapper
public interface BlackPhoneListMapper {
    Cursor<BlackPhoneList> selectByCursor(Long temporaryId);

    int updateByPrimaryKeyBatch(@Param("record") List<BlackPhoneList> record);
}