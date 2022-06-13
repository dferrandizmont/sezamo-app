package com.dferrandizmont.sezamo.scheduler;

import com.dferrandizmont.sezamo.service.CustomerOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CheckOrderScheduler {

    @Autowired
    private CustomerOrderService orderService;

    @Scheduled(fixedRate = 60000)
    public void checkOrdersToExpire() {
       this.orderService.checkOrdersToExpire();
    }
}
