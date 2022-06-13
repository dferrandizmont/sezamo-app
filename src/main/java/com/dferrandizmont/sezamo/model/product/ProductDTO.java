package com.dferrandizmont.sezamo.model.product;

import com.dferrandizmont.sezamo.model.order.CustomerOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {

    private Long id;
    private String name;
    private Integer stock;
    private Double price;
    private CustomerOrder order;
}
