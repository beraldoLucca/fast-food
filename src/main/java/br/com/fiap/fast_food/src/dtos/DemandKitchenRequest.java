package br.com.fiap.fast_food.src.dtos;

import br.com.fiap.fast_food.src.db.models.Product;
import br.com.fiap.fast_food.src.enums.DemandStatus;

import java.util.List;

public record DemandKitchenRequest(Long id, List<Product> products, DemandStatus status) {
}
