package com.dferrandizmont.sezamo.service;

import com.dferrandizmont.sezamo.exception.ValidationException;
import com.dferrandizmont.sezamo.model.order.CustomerOrderDTOResponse;
import com.dferrandizmont.sezamo.model.enums.OrderStatus;
import com.dferrandizmont.sezamo.model.order.CustomerOrder;
import com.dferrandizmont.sezamo.model.order.CustomerOrderDTO;
import com.dferrandizmont.sezamo.model.orderproduct.OrderProductDTO;
import com.dferrandizmont.sezamo.model.product.Product;
import com.dferrandizmont.sezamo.repository.IOrderProductRepository;
import com.dferrandizmont.sezamo.repository.ICustomerOrderRepository;
import com.dferrandizmont.sezamo.repository.IProductRepository;
import com.dferrandizmont.sezamo.validator.OrderValidator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomerOrderService {

    @Autowired
    private ICustomerOrderRepository orderRepository;

    @Autowired
    private IProductRepository productRepository;

    @Autowired
    private IOrderProductRepository orderProductRepository;

    @Autowired
    private OrderValidator orderValidator;

    @Autowired
    private ModelMapper modelMapper;

    public Optional<CustomerOrder> findOrderById(Long id) {
        return this.orderRepository.findCustomerOrderById(id);
    }

    @Transactional
    public CustomerOrder createOrder(CustomerOrderDTO customerOrderDTO) {
        orderValidator.isValid(customerOrderDTO);

        var orderEntity = modelMapper.map(customerOrderDTO, CustomerOrder.class);
        orderEntity.getOrderProducts().forEach(op -> op.setCustomerOrder(orderEntity));

        var mapProductQuantity = customerOrderDTO.getOrderProducts().stream().collect(Collectors.toMap(op -> op.getProduct().getId(), OrderProductDTO::getQuantity));
        var productsToUpdate = this.productRepository.findAllById(mapProductQuantity.keySet());
        productsToUpdate.forEach(product -> {
            var quantity = mapProductQuantity.get(product.getId());
            product.decreaseStock(quantity);
        });

        return this.orderRepository.save(orderEntity);
    }

    public void checkOrdersToExpire() {
        System.out.println("Checking orders ...");
        var thirtyMinutesBefore = LocalDateTime.now().minusMinutes(30L);
        var ordersToExpire = this.orderRepository.findCustomerOrdersNotPayedAndExpired(OrderStatus.CREATED, thirtyMinutesBefore);

        if (!ordersToExpire.isEmpty()) {
            System.out.println("Found " + ordersToExpire.size() + " orders to expire ...");
            ordersToExpire.forEach(order -> order.setStatus(OrderStatus.CANCELLED));
            this.orderRepository.saveAll(ordersToExpire);

            var orderProducts = this.orderProductRepository.findOrderProductsByCustomerOrderIn(ordersToExpire);
            var mapProductQuantity = new HashMap<Product, Integer>();

            orderProducts.forEach(orderProduct -> {
                if (mapProductQuantity.containsKey(orderProduct.getProduct())) {
                    mapProductQuantity.put(orderProduct.getProduct(), mapProductQuantity.get(orderProduct.getProduct()) + orderProduct.getQuantity());
                } else {
                    mapProductQuantity.put(orderProduct.getProduct(), orderProduct.getQuantity());
                }
            });

            var products = new ArrayList<>(mapProductQuantity.keySet());
            products.forEach(product -> {
                product.setStock(product.getStock() + mapProductQuantity.get(product));
            });

            this.productRepository.saveAll(products);
            System.out.println("Expired " + ordersToExpire.size() + " orders ...");
        } else {
            System.out.println("No orders to expire ...");
        }
    }

    public CustomerOrder validateAndPayOrder(Long orderId) {
        var orderOptional = this.findOrderById(orderId);

        if (orderOptional.isPresent()) {
            if (orderOptional.get().getStatus() == OrderStatus.CANCELLED) {
                throw new ValidationException("Order is already cancelled");
            }

            orderOptional.get().setStatus(OrderStatus.PAID);
            return this.orderRepository.save(orderOptional.get());
        }

        return null;
    }

    public CustomerOrder validateAndCancelOrder(Long orderId) {
        var orderOptional = this.findOrderById(orderId);

        if (orderOptional.isPresent()) {
            orderOptional.get().setStatus(OrderStatus.CANCELLED);
            return this.orderRepository.save(orderOptional.get());
        }

        return null;
    }

    public CustomerOrderDTOResponse fillAllFields(CustomerOrder orderCreated) {
        var orderOptional = this.findOrderById(orderCreated.getId());
        var productIds = orderCreated.getOrderProducts().stream().map(op -> op.getProduct().getId()).collect(Collectors.toList());

        this.productRepository.findAllById(productIds).forEach(product -> {
            var orderProduct = orderCreated.getOrderProducts().stream().filter(op -> op.getProduct().getId().equals(product.getId())).findFirst();
            orderProduct.ifPresent(value -> value.setProduct(product));
        });

        return CustomerOrderDTOResponse.mapToResponse(orderCreated);
    }
}
