package br.com.fiap.fast_food.src.core.application.mapper;

import br.com.fiap.fast_food.src.adapters.driver.dto.DemandRequest;
import br.com.fiap.fast_food.src.core.domain.entities.Customer;
import br.com.fiap.fast_food.src.core.domain.entities.Demand;
import br.com.fiap.fast_food.src.core.domain.entities.Product;
import br.com.fiap.fast_food.src.core.domain.vo.Cpf;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IDemandMapper {

    IDemandMapper INSTANCE = Mappers.getMapper(IDemandMapper.class);

    Demand toEntity(DemandRequest request, List<Product> products, Customer customer);

    Cpf map(String value);

    Demand updateToEntity(Demand demand);
}
