

package com.linwen.controller.admin.service;

import com.linwen.comm.base.BaseService;
import com.linwen.service.sholls.ShollsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Created by linwen on 19-7-6.
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class ShollsAndService extends BaseService {
    @Autowired
    ShollsService shollsService;


    public ShollsService getShollsService() {
        return this.shollsService;
    }


}
