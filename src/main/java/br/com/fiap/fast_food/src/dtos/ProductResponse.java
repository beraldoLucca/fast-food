package br.com.fiap.fast_food.src.dtos;

import java.math.BigDecimal;

public record ProductResponse(String name, BigDecimal price, String description) {
}
