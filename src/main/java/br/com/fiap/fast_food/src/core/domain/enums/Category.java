package br.com.fiap.fast_food.src.core.domain.enums;

import br.com.fiap.fast_food.src.core.domain.exception.ValidationException;
import lombok.Getter;

@Getter
public enum Category {
    LANCHE(1),
    ACOMPANHAMENTO(2),
    BEBIDA(3),
    SOBREMESA(4);

    private final int value;

    Category(int value) {
        this.value = value;
    }

    public static Category fromValue(int value) {
        for (Category category : Category.values()) {
            if (category.getValue() == value) {
                return category;
            }
        }
        throw new ValidationException("Categoria invalida.");
    }
}
