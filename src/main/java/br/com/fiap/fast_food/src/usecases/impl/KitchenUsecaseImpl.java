package br.com.fiap.fast_food.src.usecases.impl;

import br.com.fiap.fast_food.src.db.models.Product;
import br.com.fiap.fast_food.src.dtos.DemandKitchenRequest;
import br.com.fiap.fast_food.src.dtos.ProductKitchenRequest;
import br.com.fiap.fast_food.src.repositories.IDemandRepository;
import br.com.fiap.fast_food.src.usecases.IKitchenUsecase;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class KitchenUsecaseImpl implements IKitchenUsecase {

    private final RestTemplate restTemplate;

    private final IDemandRepository demandRepository;

    private static final String URL_KITCHEN_SERVICE = "http://localhost:8082/kitchen/demand";
    private static final String URL_KITCHEN_SERVICE_V2 = "http://localhost:8082/kitchen/demand/{id}";

    public KitchenUsecaseImpl(RestTemplate restTemplate, IDemandRepository demandRepository) {
        this.restTemplate = restTemplate;
        this.demandRepository = demandRepository;
    }

    @Override
    public void sendDemandToKitchen(Integer id) {
        var demand = demandRepository.findById(Long.valueOf(id)).get();
        List<ProductKitchenRequest> productKitchenRequests = demand.getProducts().stream().map(product -> new ProductKitchenRequest(product.getName())).toList();
        DemandKitchenRequest demandKitchenRequest = new DemandKitchenRequest(
                demand.getId(), productKitchenRequests, demand.getStatus());
        restTemplate.postForObject(URL_KITCHEN_SERVICE, demandKitchenRequest, DemandKitchenRequest.class);
    }
}
