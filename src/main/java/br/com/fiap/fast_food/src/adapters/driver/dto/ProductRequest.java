package br.com.fiap.fast_food.src.adapters.driver.dto;

import java.math.BigDecimal;

public record ProductRequest(String name, int categoryId, BigDecimal price,
                             String description, String image) {
}
