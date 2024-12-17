package br.com.fiap.fast_food.src.adapters.driver.dto;

import br.com.fiap.fast_food.src.core.domain.entities.Customer;
import br.com.fiap.fast_food.src.core.domain.entities.Demand;
import br.com.fiap.fast_food.src.core.domain.enums.DemandStatus;

import java.time.LocalTime;
import java.util.List;

public record DemandResponse(Customer customer, List<ProductResponse> products, Double preparationTime, DemandStatus status, LocalTime createdAt) {

    public static DemandResponse of(Demand demand, List<ProductResponse> products) {
        return new DemandResponse(demand.getCustomer(), products, demand.getPreparationTime(), demand.getStatus(), demand.getCreatedAt());
    }
}
