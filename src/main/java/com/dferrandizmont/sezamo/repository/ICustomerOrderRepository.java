package com.dferrandizmont.sezamo.repository;

import com.dferrandizmont.sezamo.model.enums.OrderStatus;
import com.dferrandizmont.sezamo.model.order.CustomerOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ICustomerOrderRepository extends JpaRepository<CustomerOrder, Long> {

    Optional<CustomerOrder> findCustomerOrderById(Long id);

    @Query("SELECT o FROM CustomerOrder o WHERE o.status = ?1 and o.createdAt < ?2")
    List<CustomerOrder> findCustomerOrdersNotPayedAndExpired(OrderStatus status, LocalDateTime createdAt);
}
