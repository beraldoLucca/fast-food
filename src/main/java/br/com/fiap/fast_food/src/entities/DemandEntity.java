package br.com.fiap.fast_food.src.entities;

import br.com.fiap.fast_food.src.db.models.Customer;
import br.com.fiap.fast_food.src.db.models.Product;
import br.com.fiap.fast_food.src.enums.DemandStatus;
import br.com.fiap.fast_food.src.enums.PaymentStatus;
import br.com.fiap.fast_food.src.exceptions.ValidationException;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;
import java.util.List;

@Getter
public class DemandEntity {

    private Customer customer;

    private List<Product> products;

    private Double preparationTime;

    private LocalTime createdAt;

    private String status;

    private String paymentStatus;

    public DemandEntity(Customer customer, List<Product> products, Double preparationTime, LocalTime createdAt, DemandStatus status, PaymentStatus paymentStatus) {
        if (preparationTime <= 0){
            throw new ValidationException("Tempo de preparação deve ser maior que zero.");
        }
        if(status != DemandStatus.RECEBIDO){
            throw new ValidationException("Pedido não foi recebido ainda.");
        }
        this.customer = customer;
        this.products = products;
        this.preparationTime = preparationTime;
        this.createdAt = createdAt;
        this.status = String.valueOf(status);
        this.paymentStatus = String.valueOf(paymentStatus);
    }
}
