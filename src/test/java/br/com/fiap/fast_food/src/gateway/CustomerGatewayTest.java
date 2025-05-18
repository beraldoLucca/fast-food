package br.com.fiap.fast_food.src.gateway;

import br.com.fiap.fast_food.src.Utils;
import br.com.fiap.fast_food.src.db.models.Customer;
import br.com.fiap.fast_food.src.dtos.CustomerRequest;
import br.com.fiap.fast_food.src.entities.CustomerEntity;
import br.com.fiap.fast_food.src.gateways.ICustomerGateway;
import br.com.fiap.fast_food.src.gateways.impl.CustomerGatewayImpl;
import br.com.fiap.fast_food.src.repositories.ICustomerRepository;
import br.com.fiap.fast_food.src.vo.Cpf;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class CustomerGatewayTest {

    private ICustomerGateway customerGateway;

    @Mock
    private ICustomerRepository repository;

    AutoCloseable mock;

    @BeforeEach
    void setup(){
        mock = MockitoAnnotations.openMocks(this);
        customerGateway = new CustomerGatewayImpl(repository);
    }

    @AfterEach
    void tearDown() throws Exception {
        mock.close();
    }

    @Test
    void shouldAllowSaveCustomerSuccessfully() {
        // Arrange
        var customer = Utils.createCustomer();

        when(repository.save(customer)).thenReturn(customer);

        //Act
        customerGateway.save(customer);

        // Assert
        verify(repository, times(1)).save(any(Customer.class));
    }

    @Test
    void shouldFindCustomerByIdSuccessfully() {
        // Arrange
        var customer = Utils.createCustomer();

        when(repository.findById(any(Cpf.class))).thenReturn(Optional.of(customer));

        // Act
        var customerReturned = customerGateway.findById(customer.getCpf()).get();

        // Assert
        Assertions.assertEquals(customer.getCpf(), customerReturned.getCpf());
        Assertions.assertEquals(customer.getName(), customerReturned.getName());
        Assertions.assertEquals(customer.getEmail(), customerReturned.getEmail());
    }

    @Test
    void shouldFindAllCustomerSuccessfully() {
        // Arrange
        var customer = Utils.createCustomer();
        List<Customer> customers = List.of(customer);

        when(repository.findAll()).thenReturn(customers);

        // Act
        var customerList = customerGateway.findAll();

        // Assert
        Assertions.assertEquals(1, customerList.size());
    }
}
