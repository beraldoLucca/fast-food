package br.com.fiap.fast_food.src.adapters.driver.dto;

import br.com.fiap.fast_food.src.core.domain.entities.Customer;
import br.com.fiap.fast_food.src.core.domain.entities.Demand;
import br.com.fiap.fast_food.src.core.domain.enums.DemandStatus;

import java.util.List;

public record DemandResponse(Customer customer, List<ProductResponse> products, Double time, DemandStatus status) {

    public static DemandResponse of(Demand demand, List<ProductResponse> products) {
        return new DemandResponse(demand.getCustomer(), products, demand.getTime(), demand.getStatus());
    }
}
