package br.com.fiap.fast_food.src.usecases.impl;

import br.com.fiap.fast_food.src.adapters.IDemandAdapter;
import br.com.fiap.fast_food.src.adapters.IProductAdapter;
import br.com.fiap.fast_food.src.db.models.Customer;
import br.com.fiap.fast_food.src.db.models.Demand;
import br.com.fiap.fast_food.src.db.models.Product;
import br.com.fiap.fast_food.src.dtos.DemandRequest;
import br.com.fiap.fast_food.src.dtos.DemandResponse;
import br.com.fiap.fast_food.src.enums.DemandStatus;
import br.com.fiap.fast_food.src.enums.PaymentStatus;
import br.com.fiap.fast_food.src.exceptions.ValidationException;
import br.com.fiap.fast_food.src.gateways.ICustomerGateway;
import br.com.fiap.fast_food.src.gateways.IDemandGateway;
import br.com.fiap.fast_food.src.gateways.IProductGateway;
import br.com.fiap.fast_food.src.usecases.ICustomerUsecase;
import br.com.fiap.fast_food.src.usecases.IDemandUsecase;
import br.com.fiap.fast_food.src.usecases.IProductUsecase;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalTime;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;


@Service
public class DemandUsecaseImpl implements IDemandUsecase {

    private final ICustomerUsecase customerUsecase;

    private final IDemandAdapter demandAdapter;

    private final IProductAdapter productAdapter;

    private final IProductUsecase productUsecase;

    private static final String SECRET = "chave_secreta_do_webhook";

    public DemandUsecaseImpl(ICustomerUsecase customerUsecase, IDemandAdapter demandAdapter, IProductAdapter productAdapter, IProductUsecase productUsecase) {
        this.customerUsecase = customerUsecase;
        this.productUsecase = productUsecase;
        this.demandAdapter = demandAdapter;
        this.productAdapter = productAdapter;
    }

    @Override
    @Transactional
    public void save(DemandRequest request, ICustomerGateway customerGateway, IProductGateway productGateway, IDemandGateway demandGateway) {
        var timeRandom = ThreadLocalRandom.current().nextDouble(1.0, 15.0);
        var preparationTime = Math.round(timeRandom * 10.0) / 10.0;
        var demandStatus = DemandStatus.RECEBIDO;
        var paymentStatus = PaymentStatus.EM_ANDAMENTO;
        var createdAt = LocalTime.now();
        Customer customer = setCustomer(request, customerGateway);
        validateProducts(request);
        List<Product> productList = setProducts(request, productGateway);
        var demand = demandAdapter.toEntity(request, productList, customer, preparationTime, demandStatus, paymentStatus, createdAt);
        demandGateway.save(demand);
    }

    @Override
    @Transactional
    public void finalizeDemand(Long id, IDemandGateway gateway) {
        var demand = gateway.findById(id);
        if(demand == null){
            throw new ValidationException("Pedido não encontrado");
        }
        demand.setStatus(DemandStatus.FINALIZADO);
        gateway.save(demandAdapter.updateToEntity(demand));
    }

    @Override
    public List<DemandResponse> findAll(IDemandGateway gateway) {
        var demandList = gateway.findAllDemandsInOrder(DemandStatus.FINALIZADO, DemandStatus.PRONTO, DemandStatus.EM_PREPARACAO, DemandStatus.RECEBIDO);
        return demandList.stream().map(demand -> DemandResponse.of(demand, productAdapter.toProductResponses(demand.getProducts()))).toList();
    }

    @Override
    public Demand findDemandPaymentStatus(Long id, IDemandGateway gateway) {
        var demand = gateway.findById(id);
        if(demand == null){
            throw new ValidationException("Por favor, informe um id válido");
        }
        return demand;
    }

    @Override
    public void processPayment(Map<String, Object> payload, String signatureHeader, IDemandGateway gateway) throws NoSuchAlgorithmException, InvalidKeyException {
        validateSignature(signatureHeader, payload);

        String evento = (String) payload.get("event");
        Map<String, Object> data = (Map<String, Object>) payload.get("data");

        if ("payment_approved".equals(evento)) {
            Long demandId = (Long) data.get("order_id");
            gateway.updatePaymentStatus(demandId, PaymentStatus.APROVADO);
        } else if ("payment_failed".equals(evento)) {
            throw new ValidationException("Pagamento falhou");
        } else {
            throw new ValidationException("Evento desconhecido: " + evento);
        }
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

    private Customer setCustomer(DemandRequest request, ICustomerGateway customerGateway) {
        if(request.getCustomer() != null){
            if(request.getCustomer().cpf() != null) {
                var customer = customerUsecase.findById(request.getCustomer().cpf(), customerGateway);
                if (customer.isPresent()) {
                    return customer.get();
                } else {
                    customerUsecase.save(request.getCustomer(), customerGateway);
                    return customerUsecase.findByCpf(request.getCustomer().cpf(), customerGateway);
                }
            }
            else{
                throw new ValidationException("Por favor, preencha o CPF");
            }
        }
        return null;
    }

    private List<Product> setProducts(DemandRequest request, IProductGateway productGateway) {
        var products = productUsecase.findAllById(request.getProductsId(), productGateway);
        if (products.isEmpty()) {
            throw new ValidationException("Por favor, selecione um produto válido.");
        }
        return products;
    }

    private void validateProducts(DemandRequest request) {
        if(request.getProductsId() == null || request.getProductsId().isEmpty()){
            throw new ValidationException("Por favor, selecione pelo menos um produto.");
        }
    }
}
