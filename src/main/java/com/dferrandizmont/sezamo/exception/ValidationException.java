package com.dferrandizmont.sezamo.exception;

import com.dferrandizmont.sezamo.model.product.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;

@Getter
@Setter
@NoArgsConstructor
public class ValidationException extends RuntimeException {

    private HashMap<Product, Integer> productsNotEnoughStock;

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ValidationException(String message, HashMap<Product, Integer> mapProductsNotEnoughStock) {
        super(message);
        this.productsNotEnoughStock = mapProductsNotEnoughStock;
    }
}
