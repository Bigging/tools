package org.example.service;


import org.example.rest.api.param.RefreshBlackPhoneListReq;

public interface IRefreshDataService {

    void refreshBlackPhoneList(RefreshBlackPhoneListReq req);
}
