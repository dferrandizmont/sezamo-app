package com.dferrandizmont.sezamo.controller;

import com.dferrandizmont.sezamo.model.CustomerOrderDAO;
import com.dferrandizmont.sezamo.model.enums.OrderStatus;
import com.dferrandizmont.sezamo.model.order.CustomerOrder;
import com.dferrandizmont.sezamo.model.order.CustomerOrderDTO;
import com.dferrandizmont.sezamo.model.order.CustomerOrderDTOResponse;
import com.dferrandizmont.sezamo.model.orderproduct.OrderProductDTOResponse;
import com.dferrandizmont.sezamo.model.product.ProductDTOResponse;
import com.dferrandizmont.sezamo.repository.ICustomerOrderRepository;
import com.dferrandizmont.sezamo.service.CustomerOrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CustomerOrderController.class)
@ExtendWith(SpringExtension.class)
public class CustomOrderControllerTest {

    @MockBean
    private CustomerOrderService service;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ICustomerOrderRepository orderRepository;

    @Test
    public void shouldGetOrderById() throws Exception {
        // given
        var customerOrder = CustomerOrderDAO.getCustomerOrder();

        // when
        when(service.findOrderById(customerOrder.getId())).thenReturn(Optional.of(customerOrder));

        // then
        mockMvc.perform(get("/order/{orderId}", customerOrder.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(customerOrder.getId()))
                .andExpect(jsonPath("$.status").value(OrderStatus.CREATED.name()))
                .andExpect(jsonPath("$.orderProducts").isArray())
                .andDo(print());
    }

    @Test
    public void shouldReturnNotFoundById() throws Exception {
        // given
        var customerOrderId = 1L;

        // when
        when(service.findOrderById(customerOrderId)).thenReturn(Optional.empty());

        // then
        mockMvc.perform(get("/order/{orderId}", customerOrderId))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void shouldCreateCustomerOrder() throws Exception {
        // given
        var productDTOResponse = new ProductDTOResponse();
        productDTOResponse.setName("Product 1");
        productDTOResponse.setPrice(10.0);

        var orderProductDTOResponse = new OrderProductDTOResponse();
        orderProductDTOResponse.setProduct(productDTOResponse);
        orderProductDTOResponse.setQuantity(1);

        var customerOrderDTOResponse = new CustomerOrderDTOResponse();
        customerOrderDTOResponse.setId(1L);
        customerOrderDTOResponse.setStatus(OrderStatus.CREATED);
        customerOrderDTOResponse.setOrderProducts(List.of(orderProductDTOResponse));

        var customerOrder = CustomerOrderDAO.getCustomerOrder();

        // when
        when(service.createOrder(any(CustomerOrderDTO.class))).thenReturn(customerOrder);
        when(service.fillAllFields(any(CustomerOrder.class))).thenReturn(customerOrderDTOResponse);
        // then
        mockMvc.perform(post("/order/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new CustomerOrderDTO())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(customerOrder.getId()))
                .andExpect(jsonPath("$.status").value(OrderStatus.CREATED.name()))
                .andExpect(jsonPath("$.orderProducts").isArray())
                .andExpect(jsonPath("$.orderProducts[0].product.name").value(productDTOResponse.getName()))
                .andDo(print());
    }

    @Test
    public void shouldUpdateToCancelStatus() throws Exception {
        // given
        var customerOrder = CustomerOrderDAO.getCustomerOrder();
        var customerOrderCancelled = CustomerOrderDAO.getCustomerOrder();
        customerOrderCancelled.setStatus(OrderStatus.CANCELLED);

        // when
        when(service.validateAndCancelOrder(customerOrder.getId())).thenReturn(customerOrderCancelled);

        // then
        mockMvc.perform(put("/order/{orderId}/cancel", customerOrder.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(customerOrder.getId()))
                .andExpect(jsonPath("$.status").value(OrderStatus.CANCELLED.name()))
                .andDo(print());
    }

    @Test
    public void shouldReturnNotFoundOnUpdateToCancelStatus() throws Exception {
        // given
        var customerOrderId = 1L;

        // when
        when(service.validateAndCancelOrder(customerOrderId)).thenReturn(null);

        // then
        mockMvc.perform(put("/order/{orderId}/cancel", customerOrderId))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void shouldUpdateToPaidStatus() throws Exception {
        // given
        var customerOrder = CustomerOrderDAO.getCustomerOrder();
        var customerOrderPaid = CustomerOrderDAO.getCustomerOrder();
        customerOrderPaid.setStatus(OrderStatus.PAID);

        // when
        when(service.validateAndPayOrder(customerOrder.getId())).thenReturn(customerOrderPaid);

        // then
        mockMvc.perform(put("/order/{orderId}/pay", customerOrder.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(customerOrder.getId()))
                .andExpect(jsonPath("$.status").value(OrderStatus.PAID.name()))
                .andDo(print());
    }

    @Test
    public void shouldReturnNotFoundOnUpdateToPaidStatus() throws Exception {
        // given
        var customerOrderId = 1L;

        // when
        when(service.validateAndPayOrder(customerOrderId)).thenReturn(null);

        // then
        mockMvc.perform(put("/order/{orderId}/pay", customerOrderId))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

}
