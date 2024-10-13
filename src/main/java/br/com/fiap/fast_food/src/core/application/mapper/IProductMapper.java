package br.com.fiap.fast_food.src.core.application.mapper;

import br.com.fiap.fast_food.src.adapters.driver.dto.ProductRequest;
import br.com.fiap.fast_food.src.adapters.driver.dto.ProductResponse;
import br.com.fiap.fast_food.src.core.domain.entities.Product;
import br.com.fiap.fast_food.src.core.domain.enums.Category;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IProductMapper {

    IProductMapper INSTANCE = Mappers.getMapper(IProductMapper.class);

    Product toEntity(ProductRequest request, Category category);

    Product updateToEntity(ProductRequest request, Category category, Long id);

    List<ProductResponse> toProductResponses(List<Product> product);
}
