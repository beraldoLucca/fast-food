package br.com.fiap.fast_food.src.gateways.impl;

import br.com.fiap.fast_food.src.db.models.Demand;
import br.com.fiap.fast_food.src.enums.DemandStatus;
import br.com.fiap.fast_food.src.enums.PaymentStatus;
import br.com.fiap.fast_food.src.gateways.IDemandGateway;
import br.com.fiap.fast_food.src.repositories.IDemandRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DemandGatewayImpl implements IDemandGateway {

    private final IDemandRepository demandRepository;

    public DemandGatewayImpl(IDemandRepository demandRepository) {
        this.demandRepository = demandRepository;
    }

    @Override
    public void save(Demand demand) {
        demandRepository.save(demand);
    }

    @Override
    public List<Demand> findAll() {
        return demandRepository.findAll();
    }

    @Override
    public Demand findById(Long id) {
        return demandRepository.findById(id).orElse(null);
    }

    @Override
    public void updatePaymentStatus(Long id, PaymentStatus paymentStatus) {
        demandRepository.updatePaymentStatus(id, paymentStatus);
    }

    @Override
    public List<Demand> findAllDemandsInOrder(DemandStatus statusFinalizado, DemandStatus statusPronto, DemandStatus statusEmPreparacao, DemandStatus statusRecebido) {
        return demandRepository.findAllDemandsInOrder(statusFinalizado, statusPronto, statusEmPreparacao, statusRecebido);
    }
}
