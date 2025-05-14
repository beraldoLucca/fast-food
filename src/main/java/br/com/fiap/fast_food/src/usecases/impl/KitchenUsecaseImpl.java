package br.com.fiap.fast_food.src.usecases.impl;

import br.com.fiap.fast_food.src.dtos.DemandKitchenRequest;
import br.com.fiap.fast_food.src.repositories.IDemandRepository;
import br.com.fiap.fast_food.src.usecases.IKitchenUsecase;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class KitchenUsecaseImpl implements IKitchenUsecase {

    private final RestTemplate restTemplate;

    private final IDemandRepository demandRepository;

    private static final String URL_KITCHEN_SERVICE = "http://localhost:8082/kitchen/demand";

    public KitchenUsecaseImpl(RestTemplate restTemplate, IDemandRepository demandRepository) {
        this.restTemplate = restTemplate;
        this.demandRepository = demandRepository;
    }

    @Override
    public void sendDemandToKitchen(Integer id) {
        var demand = demandRepository.findById(Long.valueOf(id)).get();
        DemandKitchenRequest demandKitchenRequest = new DemandKitchenRequest(
                demand.getId(), demand.getProducts(), demand.getStatus()
        );
        restTemplate.postForObject(URL_KITCHEN_SERVICE, demandKitchenRequest, DemandKitchenRequest.class);
    }
}
