package com.dferrandizmont.sezamo.model.order;

import com.dferrandizmont.sezamo.model.enums.OrderStatus;
import com.dferrandizmont.sezamo.model.orderproduct.OrderProductDTO;
import com.dferrandizmont.sezamo.model.orderproduct.OrderProductDTOResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerOrderDTOResponse {

    private Long id;
    private LocalDateTime createdAt = LocalDateTime.now();
    private OrderStatus status = OrderStatus.CREATED;
    private List<OrderProductDTOResponse> orderProducts;

    public static CustomerOrderDTOResponse mapToResponse(CustomerOrder customerOrder) {
        return new CustomerOrderDTOResponse(
                customerOrder.getId(),
                customerOrder.getCreatedAt(),
                customerOrder.getStatus(),
                OrderProductDTOResponse.mapToResponseList(customerOrder.getOrderProducts())
        );
    }
}
