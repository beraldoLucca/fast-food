package br.com.fiap.fast_food.src.core.domain.entities;

import br.com.fiap.fast_food.src.core.domain.enums.DemandStatus;
import br.com.fiap.fast_food.src.core.domain.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Demand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_cpf")
    private Customer customer;

    @ManyToMany
    @JoinTable(
            name = "demand_product",
            joinColumns = @JoinColumn(name = "demand_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private List<Product> products;

    @Column
    private Double preparationTime;

    @Column
    private LocalTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column
    private DemandStatus status;

    @Enumerated(EnumType.STRING)
    @Column
    private PaymentStatus paymentStatus;
}
