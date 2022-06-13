package com.dferrandizmont.sezamo.model.orderproduct;

import com.dferrandizmont.sezamo.model.product.ProductDTOResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderProductDTOResponse {

    private ProductDTOResponse product;

    @NotNull
    @Min(1)
    private int quantity;

    public static OrderProductDTOResponse mapToResponse(OrderProduct orderProduct) {
        return new OrderProductDTOResponse(
                ProductDTOResponse.mapToResponse(orderProduct.getProduct()),
                orderProduct.getQuantity()
        );
    }

    public static List<OrderProductDTOResponse> mapToResponseList(List<OrderProduct> orderProductList) {
        var orderProductDTOResponseList = new ArrayList<OrderProductDTOResponse>();
        orderProductList.forEach(orderProduct -> orderProductDTOResponseList.add(mapToResponse(orderProduct)));
        return orderProductDTOResponseList;
    }
}
