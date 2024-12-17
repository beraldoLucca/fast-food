package br.com.fiap.fast_food.src.core.application.usecases;

import br.com.fiap.fast_food.src.adapters.driver.dto.DemandRequest;
import br.com.fiap.fast_food.src.adapters.driver.dto.DemandResponse;
import br.com.fiap.fast_food.src.core.domain.entities.Demand;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

public interface IDemandUsecase {

    void save(DemandRequest request);

    void finalizeDemand(Long id);

    List<DemandResponse> findAll();

    Demand findById(Long id);

    void processPayment(Map<String, Object> payload, String signatureHeader) throws NoSuchAlgorithmException, InvalidKeyException;
}
