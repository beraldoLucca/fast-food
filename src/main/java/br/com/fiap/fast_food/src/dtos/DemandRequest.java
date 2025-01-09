package br.com.fiap.fast_food.src.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class DemandRequest {

    private CustomerRequest customer;
    private List<Long> productsId;
}
