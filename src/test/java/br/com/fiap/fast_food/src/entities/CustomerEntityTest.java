package br.com.fiap.fast_food.src.entities;

import br.com.fiap.fast_food.src.exceptions.ValidationException;
import br.com.fiap.fast_food.src.vo.Cpf;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class CustomerEntityTest {

    @Test
    void shouldCreateValidCustomerEntity() {
        // Arrange
        Cpf cpf = new Cpf("12345678901");
        String name = "João da Silva";
        String email = "joao@email.com";

        // Act
        CustomerEntity customer = new CustomerEntity(cpf, name, email);

        // Assert
        assertThat(customer.getCpf()).isEqualTo(cpf);
        assertThat(customer.getName()).isEqualTo(name);
        assertThat(customer.getEmail()).isEqualTo(email);
    }

    @Test
    void shouldThrowExceptionWhenNameIsNull() {
        Cpf cpf = new Cpf("12345678901");
        String email = "email@email.com";

        assertThatThrownBy(() -> new CustomerEntity(cpf, null, email))
                .isInstanceOf(ValidationException.class)
                .hasMessage("Nome não pode ser nulo ou vazio");
    }

    @Test
    void shouldThrowExceptionWhenNameIsEmpty() {
        Cpf cpf = new Cpf("12345678901");
        String email = "email@email.com";

        assertThatThrownBy(() -> new CustomerEntity(cpf, "", email))
                .isInstanceOf(ValidationException.class)
                .hasMessage("Nome não pode ser nulo ou vazio");
    }

    @Test
    void shouldThrowExceptionWhenEmailIsNull() {
        Cpf cpf = new Cpf("12345678901");
        String name = "Nome";

        assertThatThrownBy(() -> new CustomerEntity(cpf, name, null))
                .isInstanceOf(ValidationException.class)
                .hasMessage("Email não pode ser nulo ou vazio");
    }

    @Test
    void shouldThrowExceptionWhenEmailIsEmpty() {
        Cpf cpf = new Cpf("12345678901");
        String name = "Nome";

        assertThatThrownBy(() -> new CustomerEntity(cpf, name, ""))
                .isInstanceOf(ValidationException.class)
                .hasMessage("Email não pode ser nulo ou vazio");
    }
}
