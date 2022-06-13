package com.dferrandizmont.sezamo.model.order;

import com.dferrandizmont.sezamo.model.enums.OrderStatus;
import com.dferrandizmont.sezamo.model.orderproduct.OrderProductDTO;
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
public class CustomerOrderDTO {

    private Long id;
    private LocalDateTime createdAt = LocalDateTime.now();
    private OrderStatus status = OrderStatus.CREATED;
    private List<OrderProductDTO> orderProducts;
}
