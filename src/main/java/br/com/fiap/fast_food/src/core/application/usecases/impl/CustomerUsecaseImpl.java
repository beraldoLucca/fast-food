package br.com.fiap.fast_food.src.core.application.usecases.impl;

import br.com.fiap.fast_food.src.adapters.driver.dto.CustomerRequest;
import br.com.fiap.fast_food.src.core.application.mapper.ICustomerMapper;
import br.com.fiap.fast_food.src.core.application.ports.repository.ICustomerRepository;
import br.com.fiap.fast_food.src.core.application.usecases.ICustomerUsecase;
import br.com.fiap.fast_food.src.core.domain.entities.Customer;
import br.com.fiap.fast_food.src.core.domain.exception.ValidationException;
import br.com.fiap.fast_food.src.core.domain.vo.Cpf;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerUsecaseImpl implements ICustomerUsecase {

    private final ICustomerRepository customerRepository;

    private final ICustomerMapper mapper;

    public CustomerUsecaseImpl(ICustomerRepository customerRepository, ICustomerMapper mapper) {
        this.customerRepository = customerRepository;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public void save(CustomerRequest request){
        Cpf cpf = new Cpf(request.cpf());
        var customer = mapper.toEntity(request);
        customerRepository.save(customer);
    };

    @Override
    public Customer findByCpf(String cpf){
        var customer = findById(cpf);
        if (customer.isEmpty()){
            throw new ValidationException("Cliente não encontrado.");
        }
        return customer.get();
    }

    @Override
    public List<Customer> findAll(){
        return customerRepository.findAll();
    }

    @Override
    public Optional<Customer> findById(String cpf) {
        return customerRepository.findById(new Cpf(cpf));
    }
}
