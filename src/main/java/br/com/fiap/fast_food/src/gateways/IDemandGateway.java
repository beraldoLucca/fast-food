package br.com.fiap.fast_food.src.gateways;

import br.com.fiap.fast_food.src.db.models.Demand;
import br.com.fiap.fast_food.src.enums.DemandStatus;
import br.com.fiap.fast_food.src.enums.PaymentStatus;
import org.springframework.data.repository.query.Param;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

public interface IDemandGateway {

    void save(Demand demand);

    List<Demand> findAll();

    Demand findById(Long id);

    void updatePaymentStatus(Long id, PaymentStatus paymentStatus) throws NoSuchAlgorithmException, InvalidKeyException;

    List<Demand> findAllDemandsInOrder(DemandStatus statusFinalizado,
                                       DemandStatus statusPronto,
                                       DemandStatus statusEmPreparacao,
                                       DemandStatus statusRecebido);
}
