package br.com.fiap.fast_food.src.controller;

import br.com.fiap.fast_food.src.Utils;
import br.com.fiap.fast_food.src.controllers.DemandController;
import br.com.fiap.fast_food.src.controllers.ProductController;
import br.com.fiap.fast_food.src.dtos.DemandRequest;
import br.com.fiap.fast_food.src.dtos.ProductRequest;
import br.com.fiap.fast_food.src.exceptions.GlobalExceptionHandler;
import br.com.fiap.fast_food.src.repositories.ICustomerRepository;
import br.com.fiap.fast_food.src.repositories.IDemandRepository;
import br.com.fiap.fast_food.src.repositories.IProductRepository;
import br.com.fiap.fast_food.src.usecases.impl.DemandUsecaseImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class DemandControllerTest {

    private DemandController demandController;

    @Mock
    private DemandUsecaseImpl demandUsecase;

    @Mock
    private ICustomerRepository customerRepository;

    @Mock
    private IProductRepository productRepository;

    @Mock
    private IDemandRepository demandRepository;

    private MockMvc mockMvc;

    AutoCloseable openMocks;

    @BeforeEach
    void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);
        DemandController demandController = new DemandController(demandUsecase, customerRepository, productRepository, demandRepository);
        mockMvc = MockMvcBuilders.standaloneSetup(demandController)
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
    void shouldAllowSaveDemandSuccessfully() throws Exception {
        var demandRequest = Utils.createDemandRequest();
        demandUsecase.save(any(DemandRequest.class), any(), any(), any());

        mockMvc.perform(post("/api/v1/demand")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(demandRequest)))
                .andExpect(status().isOk());

        verify(demandUsecase, times(1)).save(any(DemandRequest.class), any(), any(), any());
    }

    @Test
    void shouldAllowUpdateDemandSuccessfully() throws Exception {
        Long id = 1L;
        var demandRequest = Utils.createDemandRequest();
        demandUsecase.finalizeDemand(any(Long.class), any());

        mockMvc.perform(put("/api/v1/demand/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(demandRequest)))
                .andExpect(status().isOk());

        verify(demandUsecase, times(2)).finalizeDemand(any(Long.class), any());
    }

    @Test
    void shouldAllowFindAllDemandsSuccessfully() throws Exception {
        demandUsecase.findAll(any());

        mockMvc.perform(get("/api/v1/demands")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(demandUsecase, times(2)).findAll(any());
    }

    @Test
    void shouldAllowFindDemandPaymentStatusSuccessfully() throws Exception {
        Long id = 1L;
        var demand = Utils.createDemandList().get(0);
        when(demandUsecase.findDemandPaymentStatus(any(Long.class), any())).thenReturn(demand);

        mockMvc.perform(get("/api/v1/demand/{id}/payment-status", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(demandUsecase, times(1)).findDemandPaymentStatus(any(Long.class), any());
    }

    @Test
    void shouldAllowGenerateSignatureSuccessfully() throws Exception {
        Integer id = 1;
        demandUsecase.getGeneratedSignature(any(Integer.class));

        mockMvc.perform(get("/api/v1/demand/{id}/generate-signature", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(demandUsecase, times(2)).getGeneratedSignature(any(Integer.class));
    }

    @Test
    void shouldAllowHandleWebhookSuccessfully() throws Exception {
        Integer id = 1;
        Map<String, Object> payload = new HashMap<>();
        demandUsecase.processPayment(any(Map.class), any(), any(Integer.class));

        mockMvc.perform(post("/api/v1/demand/{id}/webhook/payment", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(payload)))
                .andExpect(status().isOk());

        verify(demandUsecase, times(1)).processPayment(any(Map.class), any(), any(Integer.class));
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
