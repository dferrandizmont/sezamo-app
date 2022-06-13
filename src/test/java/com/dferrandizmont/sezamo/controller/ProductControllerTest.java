package com.dferrandizmont.sezamo.controller;

import com.dferrandizmont.sezamo.model.ProductDAO;
import com.dferrandizmont.sezamo.model.product.Product;
import com.dferrandizmont.sezamo.model.product.ProductMapper;
import com.dferrandizmont.sezamo.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ProductController.class)
@ExtendWith(SpringExtension.class)
public class ProductControllerTest {

    @MockBean
    private ProductService productService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductMapper productMapper;

    @Test
    public void shouldGetProductById() throws Exception {
        // given
        var product = ProductDAO.getProduct();

        // when
        when(productService.findById(product.getId())).thenReturn(Optional.of(product));

        // then
        mockMvc.perform(get("/product/{productId}", product.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(product.getId()))
                .andExpect(jsonPath("$.name").value(product.getName()))
                .andExpect(jsonPath("$.stock").value(product.getStock()))
                .andExpect(jsonPath("$.price").value(product.getPrice()))
                .andDo(print());
    }

    @Test
    public void shouldReturnNotFoundById() throws Exception {
        // given
        var productId = 1L;

        // when
        when(productService.findById(productId)).thenReturn(Optional.empty());

        // then
        mockMvc.perform(get("/product/{productId}", productId))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void shouldCreateAProduct() throws Exception {
        // given
        var product = ProductDAO.getProductWithoutId();

        // then
        mockMvc.perform(post("/product/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    public void shouldUpdateProduct() throws Exception {
        // given
        var product = ProductDAO.getProduct();
        var modifiedProduct = ProductDAO.getProduct();
        modifiedProduct.setName("New name");

        // when
        when(productService.findById(product.getId())).thenReturn(Optional.of(product));
        when(productService.save(any(Product.class))).thenReturn(modifiedProduct);

        // then
        mockMvc.perform(put("/product/{productId}", product.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(modifiedProduct)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(modifiedProduct.getId()))
                .andExpect(jsonPath("$.name").value(modifiedProduct.getName()))
                .andExpect(jsonPath("$.stock").value(modifiedProduct.getStock()))
                .andExpect(jsonPath("$.price").value(modifiedProduct.getPrice()))
                .andDo(print());
    }

    @Test
    public void shouldReturnNotFoundOnUpdate() throws Exception {
        // given
        var modifiedProduct = ProductDAO.getProduct();
        modifiedProduct.setName("New name");

        // when
        when(productService.findById(modifiedProduct.getId())).thenReturn(Optional.empty());
        when(productService.save(any(Product.class))).thenReturn(modifiedProduct);

        // then
        mockMvc.perform(put("/product/{productId}", modifiedProduct.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(modifiedProduct)))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void shouldDeleteProduct() throws Exception {
        // given
        var product = ProductDAO.getProduct();

        // when
        when(productService.findById(product.getId())).thenReturn(Optional.of(product));
        doNothing().when(productService).delete(product);

        // then
        mockMvc.perform(delete("/product/{productId}", product.getId()))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void shouldReturnNotFoundDeleteProduct() throws Exception {
        // given
        var product = ProductDAO.getProduct();

        // when
        when(productService.findById(product.getId())).thenReturn(Optional.empty());
        doNothing().when(productService).delete(product);

        // then
        mockMvc.perform(delete("/product/{productId}", product.getId()))
                .andExpect(status().isNotFound())
                .andDo(print());
    }


}
