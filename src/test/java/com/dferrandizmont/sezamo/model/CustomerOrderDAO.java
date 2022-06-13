package com.dferrandizmont.sezamo.model;

import com.dferrandizmont.sezamo.model.enums.OrderStatus;
import com.dferrandizmont.sezamo.model.order.CustomerOrder;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class CustomerOrderDAO {


    public static CustomerOrder getCustomerOrder() {
        return new CustomerOrder(1L, LocalDateTime.now(), OrderStatus.CREATED, new ArrayList<>());
    }

    public static CustomerOrder getCustomerOrderWithoutId() {
        var customerOrder = new CustomerOrder();
        customerOrder.setOrderProducts(new ArrayList<>());

        return customerOrder;
    }
}
