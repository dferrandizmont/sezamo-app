package com.dferrandizmont.sezamo.model;

import com.dferrandizmont.sezamo.model.product.Product;

public class ProductDAO {

    public static Product getProduct() {
        return new Product(1L, "Product 1", 10, 10.0);
    }

    public static Product getProductWithoutId() {
        var product = new Product();
        product.setName("Product 1");
        product.setStock(10);
        product.setPrice(10.0);

        return product;
    }
}
