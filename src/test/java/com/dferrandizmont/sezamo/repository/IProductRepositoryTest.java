package com.dferrandizmont.sezamo.repository;

import com.dferrandizmont.sezamo.model.product.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class IProductRepositoryTest {

    @Autowired
    private IProductRepository productRepository;

    @Test
    public void contextLoads() {
        assertThat(productRepository).isNotNull();
    }

    @Test
    public void shouldFindProductById_thenReturnProduct() {
        // given
        var product = new Product();
        product.setName("Test Product");
        product.setPrice(10.0);
        product.setStock(10);
        var savedProduct = productRepository.save(product);

        // when
        var foundProduct = productRepository.findById(savedProduct.getId());

        // then
        assertThat(foundProduct).isNotEmpty();
        assertThat(foundProduct.get()).isEqualTo(savedProduct);
    }
}
