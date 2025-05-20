package br.com.fiap.fast_food.src.usecases;

import br.com.fiap.fast_food.src.Utils;
import br.com.fiap.fast_food.src.adapters.IDemandAdapter;
import br.com.fiap.fast_food.src.adapters.IProductAdapter;
import br.com.fiap.fast_food.src.db.models.Customer;
import br.com.fiap.fast_food.src.db.models.Demand;
import br.com.fiap.fast_food.src.db.models.Product;
import br.com.fiap.fast_food.src.dtos.CustomerRequest;
import br.com.fiap.fast_food.src.dtos.DemandRequest;
import br.com.fiap.fast_food.src.dtos.DemandResponse;
import br.com.fiap.fast_food.src.entities.DemandEntity;
import br.com.fiap.fast_food.src.enums.DemandStatus;
import br.com.fiap.fast_food.src.enums.PaymentStatus;
import br.com.fiap.fast_food.src.exceptions.ValidationException;
import br.com.fiap.fast_food.src.gateways.ICustomerGateway;
import br.com.fiap.fast_food.src.gateways.IDemandGateway;
import br.com.fiap.fast_food.src.gateways.IProductGateway;
import br.com.fiap.fast_food.src.usecases.impl.DemandUsecaseImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

public class DemandUsecaseImplTest {

    private DemandUsecaseImpl demandUsecase;

    @Mock
    private ICustomerUsecase customerUsecase;

    @Mock
    private IDemandAdapter demandAdapter;

    @Mock
    private IProductAdapter productAdapter;

    @Mock
    private IProductUsecase productUsecase;

    @Mock
    private IKitchenUsecase kitchenUsecase;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ICustomerGateway customerGateway;

    @Mock
    private IProductGateway productGateway;

    @Mock
    private IDemandGateway demandGateway;


    AutoCloseable mock;

    @BeforeEach
    void setup(){
        mock = MockitoAnnotations.openMocks(this);
        demandUsecase = new DemandUsecaseImpl(customerUsecase, demandAdapter, productAdapter, productUsecase, kitchenUsecase, restTemplate);
    }

    @AfterEach
    void tearDown() throws Exception {
        mock.close();
    }

    @Test
    void shouldAllowSaveDemandSuccessfully(){
        // Arrange
        var demandRequest = Utils.createDemandRequest();
        var customer = Utils.createCustomer();
        var product = Utils.createProduct();

        when(customerUsecase.findById("12345678910", customerGateway)).thenReturn(Optional.of(customer));
        productUsecase.validateProduct(demandRequest.getProductsId());
        when(productUsecase.findAllById(demandRequest.getProductsId(), productGateway)).thenReturn(List.of(product));

        var demandEntity = getEntity(customer, List.of(product));
        var demand = new Demand(1L, demandEntity.getCustomer(), demandEntity.getProducts(),
                demandEntity.getPreparationTime(), demandEntity.getCreatedAt(), DemandStatus.valueOf(demandEntity.getStatus()),
                PaymentStatus.valueOf(demandEntity.getPaymentStatus()));

        when(demandAdapter.toEntity(any())).thenReturn(demand);

        // Act
        demandUsecase.save(demandRequest, customerGateway, productGateway, demandGateway);

        // Assert
        verify(demandGateway, times(1)).save(demand);
    }

    @Test
    void shouldAllowSaveDemandWithNullCustomerSuccessfully(){
        // Arrange
        var demandRequest = Utils.createDemandRequest();
        demandRequest.setCustomer(null);
        var product = Utils.createProduct();

        productUsecase.validateProduct(demandRequest.getProductsId());
        when(productUsecase.findAllById(demandRequest.getProductsId(), productGateway)).thenReturn(List.of(product));

        var demandEntity = getEntity(null, List.of(product));
        var demand = new Demand(1L, demandEntity.getCustomer(), demandEntity.getProducts(),
                demandEntity.getPreparationTime(), demandEntity.getCreatedAt(), DemandStatus.valueOf(demandEntity.getStatus()),
                PaymentStatus.valueOf(demandEntity.getPaymentStatus()));

        when(demandAdapter.toEntity(any())).thenReturn(demand);

        // Act
        demandUsecase.save(demandRequest, customerGateway, productGateway, demandGateway);

        // Assert
        verify(demandGateway, times(1)).save(demand);
    }

    @Test
    void shouldThrowExceptionWhenCustomerCpfIsNull(){
        // Arrange
        var customerRequest = new CustomerRequest(null, null, null);
        var demandRequest = new DemandRequest();
        demandRequest.setCustomer(customerRequest);

        // Act and Assert
        assertThatThrownBy(() -> demandUsecase.save(demandRequest, customerGateway, productGateway, demandGateway))
                .isInstanceOf(ValidationException.class)
                .hasMessage("Por favor, preencha o CPF");
    }

    @Test
    void shouldThrowExceptionWhenProductsListIsEmpty(){
        // Arrange
        var demandRequest = Utils.createDemandRequest();
        var customer = Utils.createCustomer();

        when(customerUsecase.findById("12345678910", customerGateway)).thenReturn(Optional.of(customer));
        productUsecase.validateProduct(demandRequest.getProductsId());
        when(productUsecase.findAllById(demandRequest.getProductsId(), productGateway)).thenReturn(new ArrayList<>());

        // Act and Assert
        assertThatThrownBy(() -> demandUsecase.save(demandRequest, customerGateway, productGateway, demandGateway))
                .isInstanceOf(ValidationException.class)
                .hasMessage("Por favor, selecione um produto válido.");
    }

    @Test
    void shouldAllowSaveDemandWithCustomerNotPresentSuccessfully(){
        // Arrange
        var demandRequest = Utils.createDemandRequest();
        var customer = Utils.createCustomer();
        var product = Utils.createProduct();
        var customerRequest = new CustomerRequest("12345678910", "John Doe", "john.doe@gmail.com");

        when(customerUsecase.findById("12345678910", customerGateway)).thenReturn(Optional.empty());
        customerUsecase.save(customerRequest, customerGateway);
        when(customerUsecase.findByCpf("12345678910", customerGateway)).thenReturn(customer);
        productUsecase.validateProduct(demandRequest.getProductsId());
        when(productUsecase.findAllById(demandRequest.getProductsId(), productGateway)).thenReturn(List.of(product));

        var demandEntity = getEntity(customer, List.of(product));
        var demand = new Demand(1L, demandEntity.getCustomer(), demandEntity.getProducts(),
                demandEntity.getPreparationTime(), demandEntity.getCreatedAt(), DemandStatus.valueOf(demandEntity.getStatus()),
                PaymentStatus.valueOf(demandEntity.getPaymentStatus()));

        when(demandAdapter.toEntity(any())).thenReturn(demand);

        // Act
        demandUsecase.save(demandRequest, customerGateway, productGateway, demandGateway);

        // Assert
        verify(demandGateway, times(1)).save(demand);
    }

    @Test
    void shouldAllowFinalizeDemand(){
        // Arrange
        var demand = new Demand();
        when(demandGateway.findById(1L)).thenReturn(demand);

        // Act
        demandUsecase.finalizeDemand(1L, demandGateway);

        // Assert
        verify(demandGateway, times(1)).save(demand);
    }

    @Test
    void shouldThrowExceptionWithNullDemandInFinalizeDemand(){
        // Arrange
        when(demandGateway.findById(1L)).thenReturn(null);

        // Act and Assert
        assertThatThrownBy(() -> demandUsecase.finalizeDemand(1L, demandGateway))
                .isInstanceOf(ValidationException.class)
                .hasMessage("Pedido não encontrado");
    }

    @Test
    void shouldAllowFindAllDemands(){
        // Arrange
        var demandList = Utils.createDemandList();
        when(demandGateway.findAllDemandsInOrder(any(), any(), any(), any())).thenReturn(demandList);

        // Act
        List<DemandResponse> demandResponses = demandUsecase.findAll(demandGateway);

        // Assert
        Assertions.assertEquals(1, demandResponses.size());
    }

    @Test
    void shouldAllowfindDemandPaymentStatus(){
        // Arrange
        var demand = new Demand();
        demand.setId(1L);
        when(demandGateway.findById(1L)).thenReturn(demand);

        // Act
        Demand demandReturned = demandUsecase.findDemandPaymentStatus(1L, demandGateway);

        // Assert
        Assertions.assertEquals(demand.getId(), demandReturned.getId());
    }

    @Test
    void shouldThrowExceptionWithNullDemandInFindDemandPaymentStatus(){
        // Arrange
        when(demandGateway.findById(1L)).thenReturn(null);

        // Act and Assert
        assertThatThrownBy(() -> demandUsecase.findDemandPaymentStatus(1L, demandGateway))
                .isInstanceOf(ValidationException.class)
                .hasMessage("Por favor, informe um id válido");
    }

    @Test
    void shouldAllowProcessPayment() throws NoSuchAlgorithmException, InvalidKeyException {
        // Arrange
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("event", "payment_approved");
        payload.put("data", "{order_id=1}");
        when(restTemplate.getForObject(any(), any(), any(), any()))
                .thenReturn(Boolean.TRUE);
        demandGateway.updatePaymentStatus(1L, PaymentStatus.APROVADO);
        kitchenUsecase.sendDemandToKitchen(1);

        // Act and Arrange
        demandUsecase.processPayment(payload, demandGateway, 1);
    }

    @Test
    void shouldThrowExceptionInProcessPayment() throws NoSuchAlgorithmException, InvalidKeyException {
        // Arrange
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("event", "payment_approved");

        // Act and Arrange
        assertThatThrownBy(() -> demandUsecase.processPayment(payload, demandGateway, 1))
                .isInstanceOf(ValidationException.class)
                .hasMessage("Assinatura inválida");
    }

    @Test
    void shouldThrowExceptionWhenPaymentFailedInProcessPayment() throws NoSuchAlgorithmException, InvalidKeyException {
        // Arrange
        var demand = new Demand();
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("event", "payment_approved");
        payload.put("data", "{order_id=1}");
        when(restTemplate.getForObject(any(), any(), any(), any()))
                .thenReturn(Boolean.FALSE);
        demandGateway.updatePaymentStatus(1L, PaymentStatus.RECUSADO);
        when(demandGateway.findById(1L)).thenReturn(demand);

        // Act and Arrange
        assertThatThrownBy(() -> demandUsecase.processPayment(payload, demandGateway, 1))
                .isInstanceOf(ValidationException.class)
                .hasMessage("Pagamento falhou");
    }

    private DemandEntity getEntity(Customer customer, List<Product> productList) {
        var timeRandom = ThreadLocalRandom.current().nextDouble(1.0, 15.0);
        var preparationTime = Math.round(timeRandom * 10.0) / 10.0;
        var demandStatus = DemandStatus.RECEBIDO;
        var paymentStatus = PaymentStatus.EM_ANDAMENTO;
        var createdAt = LocalTime.now();
        return new DemandEntity(customer, productList, preparationTime, createdAt, demandStatus, paymentStatus);
    }
}
