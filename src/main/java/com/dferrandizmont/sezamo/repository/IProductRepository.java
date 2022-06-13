package com.dferrandizmont.sezamo.repository;

import com.dferrandizmont.sezamo.model.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IProductRepository extends JpaRepository<Product, Long> {

    List<Product> findProductsByIdIn(List<Long> productIds);
}
