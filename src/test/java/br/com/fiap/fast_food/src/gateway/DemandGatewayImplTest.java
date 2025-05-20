package br.com.fiap.fast_food.src.gateway;

import br.com.fiap.fast_food.src.Utils;
import br.com.fiap.fast_food.src.enums.PaymentStatus;
import br.com.fiap.fast_food.src.gateways.impl.DemandGatewayImpl;
import br.com.fiap.fast_food.src.repositories.IDemandRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class DemandGatewayImplTest {

    private DemandGatewayImpl demandGateway;

    @Mock
    private IDemandRepository demandRepository;

    AutoCloseable mock;

    @BeforeEach
    void setup(){
        mock = MockitoAnnotations.openMocks(this);
        demandGateway = new DemandGatewayImpl(demandRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        mock.close();
    }

    @Test
    void shouldAllowSaveDemand(){
        var demand = Utils.createDemandList().get(0);
        demandRepository.save(demand);

        demandGateway.save(demand);
    }

    @Test
    void shouldAllowFindAllDemand(){
        var demandList = Utils.createDemandList();
        when(demandRepository.findAll()).thenReturn(demandList);

        var demands = demandGateway.findAll();

        Assertions.assertEquals(1, demands.size());
    }

    @Test
    void shouldAllowFindDemandById(){
        var demand = Utils.createDemandList().get(0);
        when(demandRepository.findById(1L)).thenReturn(Optional.ofNullable(demand));

        var demandReturned = demandGateway.findById(1L);

        Assertions.assertEquals(demand.getId(), demandReturned.getId());
    }

    @Test
    void shouldAllowUpdatePaymentStatus(){
        demandRepository.updatePaymentStatus(1L, PaymentStatus.APROVADO);

        demandGateway.updatePaymentStatus(1L, PaymentStatus.APROVADO);
    }

    @Test
    void shouldAllowFindAllDemandsInOrder(){
        var demandList = Utils.createDemandList();
        when(demandRepository.findAllDemandsInOrder(any(), any(), any(), any())).thenReturn(demandList);

        var demands = demandGateway.findAllDemandsInOrder(any(), any(), any(), any());
        Assertions.assertEquals(1, demands.size());
    }

    @Test
    void shouldAllowFindDemandByProducts(){
        var demands = Utils.createDemandList();
        var productList = List.of(Utils.createProduct());
        when(demandRepository.findDemandByProducts(productList)).thenReturn(demands);

        var demandList = demandGateway.findDemandByProducts(productList);

        Assertions.assertEquals(1, demandList.size());
    }
}
