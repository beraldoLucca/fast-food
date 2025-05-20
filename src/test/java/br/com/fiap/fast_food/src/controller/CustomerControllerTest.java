package br.com.fiap.fast_food.src.controller;

import br.com.fiap.fast_food.src.controllers.CustomerController;
import br.com.fiap.fast_food.src.dtos.CustomerRequest;
import br.com.fiap.fast_food.src.exceptions.GlobalExceptionHandler;
import br.com.fiap.fast_food.src.gateways.impl.CustomerGatewayImpl;
import br.com.fiap.fast_food.src.repositories.ICustomerRepository;
import br.com.fiap.fast_food.src.usecases.impl.CustomerUsecaseImpl;
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
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CustomerControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CustomerUsecaseImpl customerUsecase;

    @Mock
    private ICustomerRepository customerRepository;

    AutoCloseable openMocks;

    @BeforeEach
    void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);
        CustomerController customerController = new CustomerController(customerUsecase, customerRepository);
        mockMvc = MockMvcBuilders.standaloneSetup(customerController)
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
    void shouldAllowSaveCustomerSuccessfully() throws Exception {
        var customerRequest = new CustomerRequest("12345678910", "John Doe", "john.doe@gmail.com");
        customerUsecase.save(any(CustomerRequest.class), any());

        mockMvc.perform(post("/api/v1/customer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(customerRequest)))
                .andExpect(status().isOk());

        verify(customerUsecase, times(1)).save(any(CustomerRequest.class), any());
    }

    @Test
    void shouldAllowFindCustomerByCpfSuccessfully() throws Exception {
        String cpf = "12345678910";

        mockMvc.perform(get("/api/v1/customer/{cpf}", cpf)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(customerUsecase, times(1)).findByCpf(anyString(), any());
    }

    @Test
    void shouldAllowFindAllCustomersSuccessfully() throws Exception {

        mockMvc.perform(get("/api/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(customerUsecase, times(1)).findAll(any());
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
