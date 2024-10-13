package br.com.fiap.fast_food.src.adapters.driver.dto;

import java.math.BigDecimal;

public record ProductResponse(String name, BigDecimal price, String description) {
}
