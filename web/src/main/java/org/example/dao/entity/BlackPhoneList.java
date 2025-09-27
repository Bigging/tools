package org.example.dao.entity;

import java.io.Serial;
import java.io.Serializable;

/**
 * 黑名单电话表
 * black_phone_list
 */
public class BlackPhoneList implements Serializable {
    /**
     * 主键ID
     */
    private Long temporaryId;

    /**
     * 黑名单电话
     */
    private String phone;

    @Serial
    private static final long serialVersionUID = 1L;

    public Long getTemporaryId() {
        return temporaryId;
    }

    public void setTemporaryId(Long temporaryId) {
        this.temporaryId = temporaryId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

}