package br.com.fiap.fast_food.src.adapters;

import br.com.fiap.fast_food.src.db.models.Customer;
import br.com.fiap.fast_food.src.entities.CustomerEntity;
import br.com.fiap.fast_food.src.vo.Cpf;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ICustomerAdapter {

    ICustomerAdapter INSTANCE = Mappers.getMapper(ICustomerAdapter.class);

    Customer toEntity(CustomerEntity entity);

    Cpf map(String value);
}
