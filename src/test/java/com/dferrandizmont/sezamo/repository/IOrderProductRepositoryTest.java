package com.dferrandizmont.sezamo.repository;

import com.dferrandizmont.sezamo.model.order.CustomerOrder;
import com.dferrandizmont.sezamo.model.orderproduct.OrderProduct;
import com.dferrandizmont.sezamo.model.product.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class IOrderProductRepositoryTest {

    @Autowired
    private IOrderProductRepository orderProductRepository;

    @Autowired
    private ICustomerOrderRepository customerOrderRepository;

    @Autowired
    private IProductRepository productRepository;

    @Test
    public void contextLoads() {
        assertThat(orderProductRepository).isNotNull();
        assertThat(customerOrderRepository).isNotNull();
        assertThat(productRepository).isNotNull();
    }

    @Test
    public void shouldFindOrderProductsByCustomerOrderIds_thenReturnListOfOrderProducts() {
        // given
        var product = new Product();
        product.setName("Test Product");
        product.setPrice(10.0);
        product.setStock(10);
        var savedProduct = productRepository.save(product);

        var customerOrder = new CustomerOrder();
        var orderProduct = new OrderProduct();
        orderProduct.setProduct(savedProduct);
        orderProduct.setQuantity(1);
        orderProduct.setCustomerOrder(customerOrder);

        customerOrder.setOrderProducts(List.of(orderProduct));
        var savedCustomerOrder = customerOrderRepository.save(customerOrder);

        // when
        var orderProductList = orderProductRepository.findOrderProductsByCustomerOrderIn(List.of(savedCustomerOrder));

        // then
        assertThat(orderProductList.size()).isEqualTo(1);
    }
}
