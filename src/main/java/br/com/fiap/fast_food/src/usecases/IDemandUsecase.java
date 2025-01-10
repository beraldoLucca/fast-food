package br.com.fiap.fast_food.src.usecases;

import br.com.fiap.fast_food.src.dtos.DemandRequest;
import br.com.fiap.fast_food.src.dtos.DemandResponse;
import br.com.fiap.fast_food.src.db.models.Demand;
import br.com.fiap.fast_food.src.gateways.ICustomerGateway;
import br.com.fiap.fast_food.src.gateways.IDemandGateway;
import br.com.fiap.fast_food.src.gateways.IProductGateway;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

public interface IDemandUsecase {

    void save(DemandRequest request, ICustomerGateway customerGateway, IProductGateway productGateway, IDemandGateway demandGateway);

    void finalizeDemand(Long id, IDemandGateway gateway);

    List<DemandResponse> findAll(IDemandGateway gateway);

    Demand findDemandPaymentStatus(Long id, IDemandGateway gateway);

    void processPayment(Map<String, Object> payload, String signatureHeader, IDemandGateway gateway) throws NoSuchAlgorithmException, InvalidKeyException;

    String getGeneratedSignature(Integer id) throws NoSuchAlgorithmException, InvalidKeyException;
}
