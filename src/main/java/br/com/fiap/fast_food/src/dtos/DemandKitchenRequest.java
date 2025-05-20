package br.com.fiap.fast_food.src.dtos;

import br.com.fiap.fast_food.src.enums.DemandStatus;

import java.util.List;

public record DemandKitchenRequest(Long orderId, List<ProductKitchenRequest> products, DemandStatus status) {
}
