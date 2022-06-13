package com.dferrandizmont.sezamo.controller;

import com.dferrandizmont.sezamo.exception.ValidationException;
import com.dferrandizmont.sezamo.exception.error.ErrorResponse;
import com.dferrandizmont.sezamo.model.order.CustomerOrder;
import com.dferrandizmont.sezamo.model.order.CustomerOrderDTO;
import com.dferrandizmont.sezamo.model.order.CustomerOrderDTOResponse;
import com.dferrandizmont.sezamo.service.CustomerOrderService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/order")
public class CustomerOrderController {

    @Autowired
    private CustomerOrderService orderService;

    @GetMapping("/{orderId}")
    public ResponseEntity<CustomerOrder> findOrderById(@PathVariable Long orderId) {
        var orderOptional = orderService.findOrderById(orderId);

        return orderOptional.map(order -> new ResponseEntity<>(order, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/create")
    public ResponseEntity<CustomerOrderDTOResponse> createOrder(@RequestBody CustomerOrderDTO order) {
        var orderCreated = orderService.createOrder(order);
        var orderResponse = orderService.fillAllFields(orderCreated);
        return new ResponseEntity<>(orderResponse, HttpStatus.CREATED);
    }

    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<CustomerOrder> cancelOrder(@PathVariable Long orderId) {
        var customerOrder = orderService.validateAndCancelOrder(orderId);
        if (Objects.nonNull(customerOrder)) {
            return new ResponseEntity<>(customerOrder, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{orderId}/pay")
    public ResponseEntity<CustomerOrder> payOrder(@PathVariable Long orderId) {
        var customerOrder = orderService.validateAndPayOrder(orderId);
        if (Objects.nonNull(customerOrder)) {
            return new ResponseEntity<>(customerOrder, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(@NotNull ValidationException ex) {
        var errorResponse = new ErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY.value(), ex.getMessage());

        if (!CollectionUtils.isEmpty(ex.getProductsNotEnoughStock())) {
            ex.getProductsNotEnoughStock()
                    .forEach((product, quantity) ->
                            errorResponse.addValidationProductError(product.getName(), "Not enough stock for product " + product.getName() + " (" + quantity + ")")
                    );
        }

        return ResponseEntity.unprocessableEntity().body(errorResponse);
    }
}
