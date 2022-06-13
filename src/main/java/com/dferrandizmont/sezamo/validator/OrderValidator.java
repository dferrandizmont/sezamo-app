package com.dferrandizmont.sezamo.validator;

import com.dferrandizmont.sezamo.exception.ValidationException;
import com.dferrandizmont.sezamo.model.orderproduct.OrderProduct;
import com.dferrandizmont.sezamo.model.order.CustomerOrderDTO;
import com.dferrandizmont.sezamo.model.orderproduct.OrderProductDTO;
import com.dferrandizmont.sezamo.model.product.Product;
import com.dferrandizmont.sezamo.repository.IProductRepository;
import com.dferrandizmont.sezamo.service.ProductService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

@Service
public class OrderValidator {

    @Autowired
    private ProductService productService;

    public void isValid(CustomerOrderDTO order) {
        if (CollectionUtils.isEmpty(order.getOrderProducts())) {
            throw new ValidationException("Order must have at least one product");
        }

        checkEnoughStock(order);
    }

    private void checkEnoughStock(CustomerOrderDTO order) {
        var mapProducts = order.getOrderProducts().stream().collect(Collectors.toMap(op -> op.getProduct().getId(), OrderProductDTO::getQuantity));
        var actualProductListStock = productService.findProductsByIdIn(new ArrayList<>(mapProducts.keySet()));
        var mapProductsNotEnoughStock = new HashMap<Product, Integer>();

        for (var product : actualProductListStock) {
            var quantity = mapProducts.get(product.getId());
            if (!product.hasEnoughStock(quantity)) {
                mapProductsNotEnoughStock.put(product, quantity);
            }
        }

        if (!mapProductsNotEnoughStock.isEmpty()) {
            throw new ValidationException("Not enough stock for the following products", mapProductsNotEnoughStock);
        }
    }
}
