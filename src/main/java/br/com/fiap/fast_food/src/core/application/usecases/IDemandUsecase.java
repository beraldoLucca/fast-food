package br.com.fiap.fast_food.src.core.application.usecases;

import br.com.fiap.fast_food.src.adapters.driver.dto.DemandRequest;
import br.com.fiap.fast_food.src.adapters.driver.dto.DemandResponse;
import br.com.fiap.fast_food.src.core.domain.entities.Demand;

import java.util.List;

public interface IDemandUsecase {

    void save(DemandRequest request);

    void finalizeDemand(Long id);

    List<DemandResponse> getAll();
}
