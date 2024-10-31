package br.com.fiap.fast_food.src.adapters.driver.dto;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ProductRequest(@NotNull(message = "Por favor, informe o nome do produto.") String name,
                             @NotNull(message = "Por favor, informe a categoria do produto.") Integer categoryId,
                             @NotNull(message = "Por favor, informe o preço do produto.") BigDecimal price,
                             @NotNull(message = "Por favor, informe a descrição do produto.") String description,
                             String image) {
}
