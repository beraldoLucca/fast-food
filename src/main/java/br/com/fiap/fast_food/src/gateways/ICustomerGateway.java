package br.com.fiap.fast_food.src.gateways;

import br.com.fiap.fast_food.src.db.models.Customer;
import br.com.fiap.fast_food.src.dtos.CustomerRequest;
import br.com.fiap.fast_food.src.vo.Cpf;

import java.util.List;
import java.util.Optional;

public interface ICustomerGateway {

    void save(Customer customer);

    List<Customer> findAll();

    Optional<Customer> findById(Cpf cpf);
}
