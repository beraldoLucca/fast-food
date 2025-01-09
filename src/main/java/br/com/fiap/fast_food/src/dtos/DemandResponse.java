package br.com.fiap.fast_food.src.dtos;

import br.com.fiap.fast_food.src.db.models.Customer;
import br.com.fiap.fast_food.src.db.models.Demand;
import br.com.fiap.fast_food.src.enums.DemandStatus;

import java.time.LocalTime;
import java.util.List;

public record DemandResponse(Customer customer, List<ProductResponse> products, Double preparationTime, DemandStatus status, LocalTime createdAt) {

    public static DemandResponse of(Demand demand, List<ProductResponse> products) {
        return new DemandResponse(demand.getCustomer(), products, demand.getPreparationTime(), demand.getStatus(), demand.getCreatedAt());
    }
}
