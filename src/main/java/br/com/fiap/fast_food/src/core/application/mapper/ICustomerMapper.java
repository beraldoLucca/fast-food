package br.com.fiap.fast_food.src.core.application.mapper;

import br.com.fiap.fast_food.src.adapters.driver.dto.CustomerRequest;
import br.com.fiap.fast_food.src.core.domain.entities.Customer;
import br.com.fiap.fast_food.src.core.domain.vo.Cpf;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ICustomerMapper {

    ICustomerMapper INSTANCE = Mappers.getMapper(ICustomerMapper.class);

    Customer toEntity(CustomerRequest customerRequest);

    Cpf map(String value);
}
