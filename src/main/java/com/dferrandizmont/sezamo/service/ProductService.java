package com.dferrandizmont.sezamo.service;

import com.dferrandizmont.sezamo.model.product.Product;
import com.dferrandizmont.sezamo.repository.IProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private IProductRepository productService;

    public Optional<Product> findById(Long productId) {
        return this.productService.findById(productId);
    }

    public List<Product> findProductsByIdIn(List<Long> productIds) {
        return this.productService.findProductsByIdIn(productIds);
    }

    public Product save(Product product) {
        return this.productService.save(product);
    }

    public void delete(Product product) {
        this.productService.delete(product);
    }
}
