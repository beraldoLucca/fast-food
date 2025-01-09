package br.com.fiap.fast_food.src.adapters;

import br.com.fiap.fast_food.src.db.models.Demand;
import br.com.fiap.fast_food.src.entities.DemandEntity;
import br.com.fiap.fast_food.src.vo.Cpf;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface IDemandAdapter {

    IDemandAdapter INSTANCE = Mappers.getMapper(IDemandAdapter.class);

    Demand toEntity(DemandEntity entity);

    Cpf map(String value);
}
