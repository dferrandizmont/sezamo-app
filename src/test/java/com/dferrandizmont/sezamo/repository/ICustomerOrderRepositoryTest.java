package com.dferrandizmont.sezamo.repository;

import com.dferrandizmont.sezamo.model.enums.OrderStatus;
import com.dferrandizmont.sezamo.model.order.CustomerOrder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ICustomerOrderRepositoryTest {

    @Autowired
    private ICustomerOrderRepository customerOrderRepository;

    @Test
    public void contextLoads() {
        assertThat(customerOrderRepository).isNotNull();
    }

    @Test
    public void shouldFindCustomerOrderById_thenReturnCustomerOrder() {
        // given
        var customerOrder = new CustomerOrder();
        customerOrder.setStatus(OrderStatus.CREATED);
        var savedCustomerOrder = customerOrderRepository.save(customerOrder);

        // when
        var foundCustomerOrder = customerOrderRepository.findById(savedCustomerOrder.getId());

        // then
        assertThat(foundCustomerOrder).isNotEmpty();
        assertThat(foundCustomerOrder.get()).isEqualTo(savedCustomerOrder);
    }

    @Test
    public void shouldFindCustomerOrdersNotPayedAndExpired_thenReturnCustomerOrders() {
        // given
        var customerOrder = new CustomerOrder();
        customerOrder.setStatus(OrderStatus.CREATED);
        customerOrder.setCreatedAt(LocalDateTime.now().minusMinutes(35));
        var savedCustomerOrder = customerOrderRepository.save(customerOrder);

        // when
        var thirtyMinutesBefore = LocalDateTime.now().minusMinutes(30L);
        var foundCustomerOrders = customerOrderRepository.findCustomerOrdersNotPayedAndExpired(OrderStatus.CREATED, thirtyMinutesBefore);

        // then
        assertThat(foundCustomerOrders.size()).isEqualTo(1);
        assertThat(foundCustomerOrders.get(0)).isEqualTo(savedCustomerOrder);
    }

}
