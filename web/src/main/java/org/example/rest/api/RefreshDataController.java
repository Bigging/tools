package org.example.rest.api;

import org.example.common.R;
import org.example.rest.api.param.RefreshBlackPhoneListReq;
import org.example.service.IRefreshDataService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/refresh")
public class RefreshDataController {

    @Resource
    private IRefreshDataService refreshDataService;

    @PostMapping("/refreshBlackPhoneList")
    public R refreshBlackPhoneList(RefreshBlackPhoneListReq req) {
        refreshDataService.refreshBlackPhoneList(req);
        return R.ok();
    }

}
