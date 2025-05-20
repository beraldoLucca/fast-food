package br.com.fiap.fast_food.src.controller;

import br.com.fiap.fast_food.src.Utils;
import br.com.fiap.fast_food.src.controllers.ProductController;
import br.com.fiap.fast_food.src.dtos.ProductRequest;
import br.com.fiap.fast_food.src.exceptions.GlobalExceptionHandler;
import br.com.fiap.fast_food.src.repositories.IDemandRepository;
import br.com.fiap.fast_food.src.repositories.IProductRepository;
import br.com.fiap.fast_food.src.usecases.impl.ProductUsecaseImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ProductControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ProductUsecaseImpl productUsecase;

    @Mock
    private IProductRepository productRepository;

    @Mock
    private IDemandRepository demandRepository;

    AutoCloseable openMocks;

    @BeforeEach
    void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);
        ProductController productController = new ProductController(productUsecase, productRepository, demandRepository);
        mockMvc = MockMvcBuilders.standaloneSetup(productController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .addFilter((request, response, chain) -> {
                    response.setCharacterEncoding("UTF-8");
                    chain.doFilter(request, response);
                }, "/*")
                .build();
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }

    @Test
    void shouldAllowSaveProductSuccessfully() throws Exception {
        var productRequest = Utils.createProductRequest();
        productUsecase.save(any(ProductRequest.class), any());

        mockMvc.perform(post("/api/v1/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(productRequest)))
                .andExpect(status().isOk());

        verify(productUsecase, times(1)).save(any(ProductRequest.class), any());
    }

    @Test
    void shouldAllowUpdateProductSuccessfully() throws Exception {
        var productRequest = Utils.createProductRequest();
        Integer id = 1;
        productUsecase.update(any(Integer.class), any(ProductRequest.class), any());

        mockMvc.perform(put("/api/v1/product/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(productRequest)))
                .andExpect(status().isOk());

        verify(productUsecase, times(1)).update(any(Integer.class), any(ProductRequest.class), any());
    }

    @Test
    void shouldAllowDeleteProductSuccessfully() throws Exception {
        Integer id = 1;
        productUsecase.remove(any(Integer.class), any(), any());

        mockMvc.perform(delete("/api/v1/product/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(productUsecase, times(2)).remove(any(Integer.class), any(), any());
    }

    @Test
    void shouldAllowFindProductByCategorySuccessfully() throws Exception {
        Integer id = 1;
        productUsecase.findByCategory(any(Integer.class), any());

        mockMvc.perform(get("/api/v1/product/category/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(productUsecase, times(2)).findByCategory(any(Integer.class), any());
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
