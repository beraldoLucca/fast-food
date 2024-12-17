package br.com.fiap.fast_food.src.core.application.usecases.impl;

import br.com.fiap.fast_food.src.adapters.driver.dto.DemandRequest;
import br.com.fiap.fast_food.src.adapters.driver.dto.DemandResponse;
import br.com.fiap.fast_food.src.core.application.mapper.IDemandMapper;
import br.com.fiap.fast_food.src.core.application.mapper.IProductMapper;
import br.com.fiap.fast_food.src.core.application.ports.repository.IDemandRepository;
import br.com.fiap.fast_food.src.core.application.usecases.ICustomerUsecase;
import br.com.fiap.fast_food.src.core.application.usecases.IDemandUsecase;
import br.com.fiap.fast_food.src.core.domain.entities.Customer;
import br.com.fiap.fast_food.src.core.domain.entities.Demand;
import br.com.fiap.fast_food.src.core.domain.entities.Product;
import br.com.fiap.fast_food.src.core.domain.enums.DemandStatus;
import br.com.fiap.fast_food.src.core.domain.enums.PaymentStatus;
import br.com.fiap.fast_food.src.core.domain.exception.ValidationException;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;


@Service
public class DemandUsecaseImpl implements IDemandUsecase {

    private final IDemandRepository demandRepository;

    private final ICustomerUsecase customerUsecase;

    private final IDemandMapper demandMapper;

    private final IProductMapper productMapper;

    private final ProductUsecaseImpl productUsecaseImpl;

    private static final String SECRET = "chave_secreta_do_webhook";

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
        var timeRandom = ThreadLocalRandom.current().nextDouble(1.0, 15.0);
        var time = Math.round(timeRandom * 10.0) / 10.0;
        var demandStatus = DemandStatus.RECEBIDO;
        var paymentStatus = PaymentStatus.EM_ANDAMENTO;
        Customer customer = setCustomer(request);
        validateProducts(request);
        List<Product> productList = setProducts(request);
        var demand = demandMapper.toEntity(request, productList, customer, time, demandStatus, paymentStatus);
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
    public List<DemandResponse> findAll() {
        var demandList = demandRepository.findAll();
        return demandList.stream().map(demand -> DemandResponse.of(demand, productMapper.toProductResponses(demand.getProducts()))).toList();
    }

    @Override
    public Demand findById(Long id) {
        var demand = demandRepository.findById(id);
        if(demand.isEmpty()){
            throw new ValidationException("Por favor, informe um id válido");
        }
        return demand.get();
    }

    @Override
    public void processPayment(Map<String, Object> payload, String signatureHeader) throws NoSuchAlgorithmException, InvalidKeyException {
        validateSignature(signatureHeader, payload);
        
    }

    private void validateSignature(String signatureHeader, Map<String, Object> payload) throws NoSuchAlgorithmException, InvalidKeyException {
        String payloadString = payload.toString();

        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKeySpec = new SecretKeySpec(SECRET.getBytes(), "HmacSHA256");
        mac.init(secretKeySpec);

        byte[] computedHash = mac.doFinal(payloadString.getBytes());
        String computedSignature = Base64.getEncoder().encodeToString(computedHash);

        if (!computedSignature.equals(signatureHeader)) {
            throw new ValidationException("Assinatura inválida");
        }
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
