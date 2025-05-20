package br.com.fiap.fast_food.src.entities;

import br.com.fiap.fast_food.src.enums.Category;
import br.com.fiap.fast_food.src.exceptions.ValidationException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ProductEntityTest {

    @Test
    void shouldCreateValidProductEntity() {
        ProductEntity product = new ProductEntity(
                "Hambúrguer",
                Category.LANCHE,
                new BigDecimal("15.99"),
                "Delicioso hambúrguer artesanal",
                "imagem.jpg"
        );

        assertThat(product.getName()).isEqualTo("Hambúrguer");
        assertThat(product.getCategory()).isEqualTo(Category.LANCHE);
        assertThat(product.getPrice()).isEqualByComparingTo("15.99");
        assertThat(product.getDescription()).isEqualTo("Delicioso hambúrguer artesanal");
        assertThat(product.getImage()).isEqualTo("imagem.jpg");
    }

    @Test
    void shouldThrowIfNameIsEmpty() {
        assertThatThrownBy(() -> new ProductEntity(
                "",
                Category.BEBIDA,
                new BigDecimal("5.00"),
                "Refrigerante gelado",
                "bebida.jpg"
        )).isInstanceOf(ValidationException.class)
                .hasMessage("Nome não pode ser vazio");
    }

    @Test
    void shouldThrowIfDescriptionIsEmpty() {
        assertThatThrownBy(() -> new ProductEntity(
                "Refrigerante",
                Category.BEBIDA,
                new BigDecimal("5.00"),
                "",
                "bebida.jpg"
        )).isInstanceOf(ValidationException.class)
                .hasMessage("Descrição não pode ser vazia");
    }

    @Test
    void shouldThrowIfPriceIsNegative() {
        assertThatThrownBy(() -> new ProductEntity(
                "Água",
                Category.BEBIDA,
                new BigDecimal("-1.00"),
                "Água mineral",
                "agua.jpg"
        )).isInstanceOf(ValidationException.class)
                .hasMessage("Preço não pode ser menor que zero");
    }
}
