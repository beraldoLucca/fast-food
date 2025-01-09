package br.com.fiap.fast_food.src.adapters;

import br.com.fiap.fast_food.src.dtos.ProductRequest;
import br.com.fiap.fast_food.src.dtos.ProductResponse;
import br.com.fiap.fast_food.src.db.models.Product;
import br.com.fiap.fast_food.src.entities.ProductEntity;
import br.com.fiap.fast_food.src.enums.Category;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IProductAdapter {

    IProductAdapter INSTANCE = Mappers.getMapper(IProductAdapter.class);

    Product toEntity(ProductEntity entity);

    Product updateToEntity(ProductEntity entity, Long id);

    List<ProductResponse> toProductResponses(List<Product> product);
}
