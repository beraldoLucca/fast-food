package br.com.fiap.fast_food.src.usecases;

import br.com.fiap.fast_food.src.dtos.CustomerRequest;
import br.com.fiap.fast_food.src.db.models.Customer;
import br.com.fiap.fast_food.src.gateways.ICustomerGateway;

import java.util.List;
import java.util.Optional;

public interface ICustomerUsecase {

    void save(CustomerRequest request, ICustomerGateway gateway);

    Customer findByCpf(String cpf, ICustomerGateway gateway);

    List<Customer> findAll(ICustomerGateway gateway);

    Optional<Customer> findById(String cpf, ICustomerGateway gateway);
}
