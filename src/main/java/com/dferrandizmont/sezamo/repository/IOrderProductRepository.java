package com.dferrandizmont.sezamo.repository;

import com.dferrandizmont.sezamo.model.orderproduct.OrderProduct;
import com.dferrandizmont.sezamo.model.order.CustomerOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IOrderProductRepository extends JpaRepository<OrderProduct, Long> {

    List<OrderProduct> findOrderProductsByCustomerOrderIn(List<CustomerOrder> orders);
}
