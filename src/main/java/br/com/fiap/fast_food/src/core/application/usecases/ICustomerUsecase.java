package br.com.fiap.fast_food.src.core.application.usecases;

import br.com.fiap.fast_food.src.adapters.driver.dto.CustomerRequest;
import br.com.fiap.fast_food.src.core.domain.entities.Customer;

import java.util.List;
import java.util.Optional;

public interface ICustomerUsecase {

    void save(CustomerRequest request);

    Customer findByCpf(String cpf);

    List<Customer> findAll();

    Optional<Customer> findById(String cpf);
}
