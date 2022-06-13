package com.dferrandizmont.sezamo.model.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTOResponse {

    private String name;
    private Double price;

    public static ProductDTOResponse mapToResponse(Product product) {
        return new ProductDTOResponse(product.getName(), product.getPrice());
    }
}
