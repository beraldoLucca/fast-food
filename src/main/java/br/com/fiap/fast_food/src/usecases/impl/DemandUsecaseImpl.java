package br.com.fiap.fast_food.src.usecases.impl;

import br.com.fiap.fast_food.src.adapters.IDemandAdapter;
import br.com.fiap.fast_food.src.adapters.IProductAdapter;
import br.com.fiap.fast_food.src.configurations.rest.RestTemplateConfig;
import br.com.fiap.fast_food.src.db.models.Customer;
import br.com.fiap.fast_food.src.db.models.Demand;
import br.com.fiap.fast_food.src.db.models.Product;
import br.com.fiap.fast_food.src.dtos.DemandRequest;
import br.com.fiap.fast_food.src.dtos.DemandResponse;
import br.com.fiap.fast_food.src.entities.DemandEntity;
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
import org.springframework.web.client.RestTemplate;

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

    private final RestTemplate restTemplate;

    private static final String SECRET = "chave_secreta_do_webhook";

    private static final String URL_PAYMENT_SERVICE = "http://localhost:8081/payment/{evento}";

    public DemandUsecaseImpl(ICustomerUsecase customerUsecase, IDemandAdapter demandAdapter, IProductAdapter productAdapter, IProductUsecase productUsecase, RestTemplate restTemplate) {
        this.customerUsecase = customerUsecase;
        this.productUsecase = productUsecase;
        this.demandAdapter = demandAdapter;
        this.productAdapter = productAdapter;
        this.restTemplate = restTemplate;
    }

    @Override
    @Transactional
    public void save(DemandRequest request, ICustomerGateway customerGateway, IProductGateway productGateway, IDemandGateway demandGateway) {
        Customer customer = setCustomer(request, customerGateway);
        validateProducts(request);
        List<Product> productList = setProducts(request, productGateway);
        var entity = getEntity(customer, productList);
        var demand = demandAdapter.toEntity(entity);
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
        gateway.save(demand);
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
    @Transactional
    public void processPayment(Map<String, Object> payload, IDemandGateway gateway, Integer id) throws NoSuchAlgorithmException, InvalidKeyException {
        var generatedSignature = getGeneratedSignature(id);
        validateSignature(generatedSignature, payload);

        String evento = (String) payload.get("event");
        Map<String, Object> data = (Map<String, Object>) payload.get("data");

        var orderPaid = restTemplate.getForObject(URL_PAYMENT_SERVICE, Boolean.class, evento);

        if(Boolean.TRUE.equals(orderPaid)){
            gateway.updatePaymentStatus(Long.valueOf(id), PaymentStatus.APROVADO);
        } else {
            gateway.updatePaymentStatus(Long.valueOf(id), PaymentStatus.RECUSADO);
            this.cancelDemand(Long.valueOf(id), gateway);
            throw new ValidationException("Pagamento falhou");
        }

//        if ("payment_approved".equals(evento)) {
//            var demandId = data.get("order_id");
//            gateway.updatePaymentStatus(Long.valueOf(demandId.toString()), PaymentStatus.APROVADO);
//        } else if ("payment_failed".equals(evento)) {
//            throw new ValidationException("Pagamento falhou");
//        } else {
//            throw new ValidationException("Evento desconhecido: " + evento);
//        }
    }

    @Override
    public String getGeneratedSignature(Integer id) throws NoSuchAlgorithmException, InvalidKeyException {
        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKeySpec = new SecretKeySpec(SECRET.getBytes(), "HmacSHA256");
        mac.init(secretKeySpec);

        String payload = "{event=payment_approved, data={order_id="+id+"}}";
        byte[] computedHash = mac.doFinal(payload.getBytes());
        return Base64.getEncoder().encodeToString(computedHash);
    }

    @Override
    @Transactional
    public void cancelDemand(Long id, IDemandGateway gateway) {
        var demand = gateway.findById(id);
        if(demand == null){
            throw new ValidationException("Pedido não encontrado");
        }
        demand.setStatus(DemandStatus.CANCELADO);
        gateway.save(demand);
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
        productUsecase.validateProduct(request.getProductsId());
    }

    private DemandEntity getEntity(Customer customer, List<Product> productList) {
        var timeRandom = ThreadLocalRandom.current().nextDouble(1.0, 15.0);
        var preparationTime = Math.round(timeRandom * 10.0) / 10.0;
        var demandStatus = DemandStatus.RECEBIDO;
        var paymentStatus = PaymentStatus.EM_ANDAMENTO;
        var createdAt = LocalTime.now();
        return new DemandEntity(customer, productList, preparationTime, createdAt, demandStatus, paymentStatus);
    }
}
