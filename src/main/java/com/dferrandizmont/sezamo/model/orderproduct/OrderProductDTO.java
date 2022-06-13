package com.dferrandizmont.sezamo.model.orderproduct;

import com.dferrandizmont.sezamo.model.product.ProductDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderProductDTO {

    private ProductDTO product;

    @NotNull
    @Min(1)
    private int quantity;
}
