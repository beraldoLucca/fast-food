package br.com.fiap.fast_food.src.usecases;

import br.com.fiap.fast_food.src.adapters.ICustomerAdapter;
import br.com.fiap.fast_food.src.db.models.Customer;
import br.com.fiap.fast_food.src.dtos.CustomerRequest;
import br.com.fiap.fast_food.src.entities.CustomerEntity;
import br.com.fiap.fast_food.src.exceptions.ValidationException;
import br.com.fiap.fast_food.src.gateways.ICustomerGateway;
import br.com.fiap.fast_food.src.usecases.impl.CustomerUsecaseImpl;
import br.com.fiap.fast_food.src.vo.Cpf;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CustomerUsecaseImplTest {

    private ICustomerUsecase customerUsecase;

    @Mock
    private ICustomerAdapter customerAdapter;

    @Mock
    private ICustomerGateway customerGateway;

    AutoCloseable mock;

    @BeforeEach
    void setup(){
        mock = MockitoAnnotations.openMocks(this);
        customerUsecase = new CustomerUsecaseImpl(customerAdapter);
    }

    @AfterEach
    void tearDown() throws Exception {
        mock.close();
    }


    @Test
    void shouldAllowSaveCustomerSuccessfully() {
        // Arrange
        var request = new CustomerRequest("12345678910", "John Doe", "john.doe@gmail.com");
        var entity = new CustomerEntity(new Cpf(request.cpf()), request.name(), request.email());
        var customer = new Customer(entity.getCpf(), entity.getName(), entity.getEmail());

        when(customerAdapter.toEntity(any(CustomerEntity.class))).thenReturn(customer);
        doNothing().when(customerGateway).save(any(Customer.class));

        //Act
        customerUsecase.save(request, customerGateway);

        // Assert
        verify(customerAdapter, times(1)).toEntity(any(CustomerEntity.class));
        verify(customerGateway, times(1)).save(any(Customer.class));
    }

    @Test
    void shouldFindCustomerByCpfSuccessfully() {
        // Arrange
        var customer = createCustomer();
        when(customerUsecase.findById("12345678910", customerGateway)).thenReturn(Optional.of(customer));

        // Act
        Customer customerFound = customerUsecase.findByCpf("12345678910", customerGateway);

        // Assert
        Assertions.assertEquals(customerFound.getCpf(), customer.getCpf());
        Assertions.assertEquals(customerFound.getName(), customer.getName());
        Assertions.assertEquals(customerFound.getEmail(), customer.getEmail());
    }

    @Test
    void shouldThrowExceptionWhenCustomerNotFound() {
        // Arrange
        when(customerUsecase.findById("12345678910", customerGateway)).thenReturn(Optional.empty());

        // Act and Assert
        assertThatThrownBy(() -> customerUsecase.findByCpf("12345678910", customerGateway))
                .isInstanceOf(ValidationException.class)
                .hasMessage("Cliente não encontrado.");

        verify(customerGateway, times(1)).findById(new Cpf("12345678910"));
    }

    @Test
    void shouldThrowExceptionWhenCustomerNotFoundUsingJunit() {
        // Arrange
        when(customerUsecase.findById("12345678910", customerGateway)).thenReturn(Optional.empty());

        // Act and Assert
        Assertions.assertThrows(ValidationException.class, () -> {
            customerUsecase.findByCpf("12345678910", customerGateway);
        });

        verify(customerGateway, times(1)).findById(new Cpf("12345678910"));
    }

    @Test
    void shouldFindAllCustomerSuccessfully() {
        // Arrange
        var customer = createCustomer();
        List<Customer> customers = List.of(customer);

        when(customerGateway.findAll()).thenReturn(customers);

        // Act
        var customerList = customerUsecase.findAll(customerGateway);

        // Assert
        Assertions.assertEquals(1, customerList.size());
    }

    private Customer createCustomer(){
        Cpf cpf = new Cpf("12345678910");
        var customer = new Customer();
        customer.setCpf(cpf);
        customer.setName("John Doe");
        customer.setEmail("john.doe@gmail.com");
        return customer;
    }
}
