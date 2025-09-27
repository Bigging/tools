package org.example.manager;


import org.example.dao.entity.BlackPhoneList;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.cursor.Cursor;

import java.util.List;

public interface BlackPhoneListManager {

    Cursor<BlackPhoneList> selectByCursor(Long temporaryId);

    int updateByPrimaryKeyBatch(@Param("record") List<BlackPhoneList> record);
}
