package br.com.fiap.fast_food.src.usecases;

import br.com.fiap.fast_food.src.dtos.DemandRequest;
import br.com.fiap.fast_food.src.dtos.DemandResponse;
import br.com.fiap.fast_food.src.db.models.Demand;
import br.com.fiap.fast_food.src.gateways.ICustomerGateway;
import br.com.fiap.fast_food.src.gateways.IProductGateway;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

public interface IDemandUsecase {

    void save(DemandRequest request, ICustomerGateway customerGateway, IProductGateway productGateway);

    void finalizeDemand(Long id);

    List<DemandResponse> findAll();

    Demand findById(Long id);

    void processPayment(Map<String, Object> payload, String signatureHeader) throws NoSuchAlgorithmException, InvalidKeyException;
}
