package br.com.fiap.fast_food.src.adapters.driver.dto;

import br.com.fiap.fast_food.src.core.domain.entities.Customer;
import br.com.fiap.fast_food.src.core.domain.entities.Product;
import br.com.fiap.fast_food.src.core.domain.enums.DemandStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class DemandRequest {

    private CustomerRequest customer;
    private List<Long> productsId;
}
