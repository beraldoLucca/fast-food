package br.com.fiap.fast_food.src.entities;

import br.com.fiap.fast_food.src.db.models.Customer;
import br.com.fiap.fast_food.src.db.models.Product;
import br.com.fiap.fast_food.src.enums.DemandStatus;
import br.com.fiap.fast_food.src.enums.PaymentStatus;
import br.com.fiap.fast_food.src.exceptions.ValidationException;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class DemandEntityTest {

    @Test
    void shouldCreateValidDemandEntity() {
        Customer customer = new Customer(); // ou mock(customer)
        List<Product> products = Collections.singletonList(new Product());
        Double preparationTime = 10.0;
        LocalTime createdAt = LocalTime.now();

        DemandEntity demand = new DemandEntity(
                customer,
                products,
                preparationTime,
                createdAt,
                DemandStatus.RECEBIDO,
                PaymentStatus.EM_ANDAMENTO
        );

        assertThat(demand.getCustomer()).isEqualTo(customer);
        assertThat(demand.getProducts()).isEqualTo(products);
        assertThat(demand.getPreparationTime()).isEqualTo(preparationTime);
        assertThat(demand.getCreatedAt()).isEqualTo(createdAt);
        assertThat(demand.getStatus()).isEqualTo("RECEBIDO");
        assertThat(demand.getPaymentStatus()).isEqualTo("EM_ANDAMENTO");
    }

    @Test
    void shouldThrowIfPreparationTimeIsZeroOrNegative() {
        Customer customer = new Customer();
        List<Product> products = Collections.singletonList(new Product());
        LocalTime createdAt = LocalTime.now();

        assertThatThrownBy(() -> new DemandEntity(
                customer,
                products,
                0.0,
                createdAt,
                DemandStatus.RECEBIDO,
                PaymentStatus.EM_ANDAMENTO
        )).isInstanceOf(ValidationException.class)
                .hasMessage("Tempo de preparação deve ser maior que zero.");

        assertThatThrownBy(() -> new DemandEntity(
                customer,
                products,
                -5.0,
                createdAt,
                DemandStatus.RECEBIDO,
                PaymentStatus.EM_ANDAMENTO
        )).isInstanceOf(ValidationException.class)
                .hasMessage("Tempo de preparação deve ser maior que zero.");
    }

    @Test
    void shouldThrowIfStatusIsNotRecebido() {
        Customer customer = new Customer();
        List<Product> products = Collections.singletonList(new Product());
        Double preparationTime = 5.0;
        LocalTime createdAt = LocalTime.now();

        assertThatThrownBy(() -> new DemandEntity(
                customer,
                products,
                preparationTime,
                createdAt,
                DemandStatus.EM_PREPARACAO,
                PaymentStatus.EM_ANDAMENTO
        )).isInstanceOf(ValidationException.class)
                .hasMessage("Pedido não foi recebido ainda.");
    }
}
