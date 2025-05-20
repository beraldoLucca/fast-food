package br.com.fiap.fast_food.src.usecases;

import br.com.fiap.fast_food.src.Utils;
import br.com.fiap.fast_food.src.repositories.IDemandRepository;
import br.com.fiap.fast_food.src.usecases.impl.KitchenUsecaseImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class KitchenUsecaseImplTest {

    private IKitchenUsecase kitchenUsecase;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private IDemandRepository demandRepository;

    AutoCloseable mock;

    @BeforeEach
    void setup(){
        mock = MockitoAnnotations.openMocks(this);
        kitchenUsecase = new KitchenUsecaseImpl(restTemplate, demandRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        mock.close();
    }

    @Test
    void shouldAllowSendDemandToKitchen(){
        var demandId = 1;
        var demand = Utils.createDemandList().get(0);
        when(demandRepository.findById(1L)).thenReturn(Optional.of(demand));
        restTemplate.postForObject(any(), any(), any());

        kitchenUsecase.sendDemandToKitchen(demandId);
    }
}
