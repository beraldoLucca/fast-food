package br.com.fiap.fast_food.src.usecases.impl;

import br.com.fiap.fast_food.src.dtos.CustomerRequest;
import br.com.fiap.fast_food.src.adapters.ICustomerAdapter;
import br.com.fiap.fast_food.src.entities.CustomerEntity;
import br.com.fiap.fast_food.src.gateways.ICustomerGateway;
import br.com.fiap.fast_food.src.repositories.ICustomerRepository;
import br.com.fiap.fast_food.src.usecases.ICustomerUsecase;
import br.com.fiap.fast_food.src.db.models.Customer;
import br.com.fiap.fast_food.src.exceptions.ValidationException;
import br.com.fiap.fast_food.src.vo.Cpf;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerUsecaseImpl implements ICustomerUsecase {

    private final ICustomerAdapter customerAdapter;

    public CustomerUsecaseImpl(ICustomerAdapter customerAdapter) {
        this.customerAdapter = customerAdapter;
    }

    @Override
    @Transactional
    public void save(CustomerRequest request, ICustomerGateway gateway){
        Cpf cpf = new Cpf(request.cpf());
        var entity = new CustomerEntity(cpf, request.name(), request.email());
        var customer = customerAdapter.toEntity(entity);
        gateway.save(customer);
    };

    @Override
    public Customer findByCpf(String cpf, ICustomerGateway gateway){
        var customer = findById(cpf, gateway);
        if (customer.isEmpty()){
            throw new ValidationException("Cliente não encontrado.");
        }
        return customer.get();
    }

    @Override
    public List<Customer> findAll(ICustomerGateway gateway){
        return gateway.findAll();
    }

    @Override
    public Optional<Customer> findById(String cpf, ICustomerGateway gateway) {
        return gateway.findById(new Cpf(cpf));
    }
}
