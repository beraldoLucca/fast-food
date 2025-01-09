package br.com.fiap.fast_food.src.gateways.impl;

import br.com.fiap.fast_food.src.db.models.Customer;
import br.com.fiap.fast_food.src.gateways.ICustomerGateway;
import br.com.fiap.fast_food.src.repositories.ICustomerRepository;
import br.com.fiap.fast_food.src.vo.Cpf;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerGatewayImpl implements ICustomerGateway {

    private final ICustomerRepository customerRepository;

    public CustomerGatewayImpl(ICustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }


    @Override
    public void save(Customer customer) {
        customerRepository.save(customer);
    }


    @Override
    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    @Override
    public Optional<Customer> findById(Cpf cpf) {
        return customerRepository.findById(cpf);
    }
}
