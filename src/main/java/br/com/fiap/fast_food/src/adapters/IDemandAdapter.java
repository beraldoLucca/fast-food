package br.com.fiap.fast_food.src.adapters;

import br.com.fiap.fast_food.src.dtos.DemandRequest;
import br.com.fiap.fast_food.src.db.models.Customer;
import br.com.fiap.fast_food.src.db.models.Demand;
import br.com.fiap.fast_food.src.db.models.Product;
import br.com.fiap.fast_food.src.enums.DemandStatus;
import br.com.fiap.fast_food.src.enums.PaymentStatus;
import br.com.fiap.fast_food.src.vo.Cpf;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.time.LocalTime;
import java.util.List;

@Mapper(componentModel = "spring")
public interface IDemandAdapter {

    IDemandAdapter INSTANCE = Mappers.getMapper(IDemandAdapter.class);

    Demand toEntity(DemandRequest request, List<Product> products, Customer customer, Double preparationTime, DemandStatus status, PaymentStatus paymentStatus, LocalTime createdAt);

    Cpf map(String value);

    Demand updateToEntity(Demand demand);
}
