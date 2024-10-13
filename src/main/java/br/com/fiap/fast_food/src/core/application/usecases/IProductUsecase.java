package br.com.fiap.fast_food.src.core.application.usecases;

import br.com.fiap.fast_food.src.adapters.driver.dto.ProductRequest;
import br.com.fiap.fast_food.src.core.domain.entities.Product;

import java.util.List;

public interface IProductUsecase {

    void save(ProductRequest product);

    void update(int id, ProductRequest product);

    void remove(int id);

    List<Product> findByCategory(int categoryId);

    List<Product> findAllById(List<Long> ids);
}
