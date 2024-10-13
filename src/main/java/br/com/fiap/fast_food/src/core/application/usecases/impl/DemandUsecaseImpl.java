package br.com.fiap.fast_food.src.core.application.usecases.impl;

import br.com.fiap.fast_food.src.adapters.driver.dto.DemandRequest;
import br.com.fiap.fast_food.src.adapters.driver.dto.DemandResponse;
import br.com.fiap.fast_food.src.core.application.mapper.IDemandMapper;
import br.com.fiap.fast_food.src.core.application.mapper.IProductMapper;
import br.com.fiap.fast_food.src.core.application.ports.repository.ICustomerRepository;
import br.com.fiap.fast_food.src.core.application.ports.repository.IDemandRepository;
import br.com.fiap.fast_food.src.core.application.ports.repository.IProductRepository;
import br.com.fiap.fast_food.src.core.application.usecases.ICustomerUsecase;
import br.com.fiap.fast_food.src.core.application.usecases.IDemandUsecase;
import br.com.fiap.fast_food.src.core.domain.entities.Customer;
import br.com.fiap.fast_food.src.core.domain.entities.Demand;
import br.com.fiap.fast_food.src.core.domain.entities.Product;
import br.com.fiap.fast_food.src.core.domain.enums.DemandStatus;
import br.com.fiap.fast_food.src.core.domain.exception.ValidationException;
import br.com.fiap.fast_food.src.core.domain.vo.Cpf;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DemandUsecaseImpl implements IDemandUsecase {

    private final IDemandRepository demandRepository;

    private final ICustomerUsecase customerUsecase;

    private final IDemandMapper demandMapper;

    private final IProductMapper productMapper;

    private final ProductUsecaseImpl productUsecaseImpl;

    public DemandUsecaseImpl(IDemandRepository demandRepository, ICustomerUsecase customerUsecase, IDemandMapper demandMapper, IProductMapper productMapper, ProductUsecaseImpl productUsecaseImpl) {
        this.demandRepository = demandRepository;
        this.customerUsecase = customerUsecase;
        this.demandMapper = demandMapper;
        this.productMapper = productMapper;
        this.productUsecaseImpl = productUsecaseImpl;
    }

    @Override
    @Transactional
    public void save(DemandRequest request) {
        Customer customer = setCustomer(request);
        validateProducts(request);
        List<Product> productList = setProducts(request);
        var demand = demandMapper.toEntity(request, productList, customer);
        demandRepository.save(demand);
    }

    @Override
    public void finalizeDemand(Long id) {
        var demand = demandRepository.findById(id);
        if(demand.isEmpty()){
            throw new ValidationException("Pedido não encontrado");
        }
        demand.get().setStatus(DemandStatus.FINALIZADO);
        demandRepository.save(demandMapper.updateToEntity(demand.get()));
    }

    @Override
    public List<DemandResponse> getAll() {
        var demandList = demandRepository.findAll();
        return demandList.stream().map(demand -> DemandResponse.of(demand, productMapper.toProductResponses(demand.getProducts()))).toList();
    }

    private Customer setCustomer(DemandRequest request) {
        if(request.getCustomer() != null){
            if(request.getCustomer().cpf() != null) {
                var customer = customerUsecase.findById(request.getCustomer().cpf());
                if (customer.isPresent()) {
                    return customer.get();
                } else {
                    customerUsecase.save(request.getCustomer());
                    return customerUsecase.findByCpf(request.getCustomer().cpf());
                }
            }
            else{
                throw new ValidationException("Por favor, preencha o CPF");
            }
        }
        return null;
    }

    private List<Product> setProducts(DemandRequest request) {
        return productUsecaseImpl.findAllById(request.getProductsId());
    }

    private void validateProducts(DemandRequest request) {
        if(request.getProductsId() == null || request.getProductsId().isEmpty()){
            throw new ValidationException("Por favor, selecione pelo menos um produto.");
        }
    }
}
