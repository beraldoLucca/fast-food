package br.com.fiap.fast_food.src.gateways;

import br.com.fiap.fast_food.src.db.models.Product;
import br.com.fiap.fast_food.src.dtos.ProductRequest;
import br.com.fiap.fast_food.src.enums.Category;

import java.util.List;
import java.util.Optional;

public interface IProductGateway {

    void save(Product product);

    void remove(Product product);

    Optional<Product> findById(Long id);

    List<Product> findByCategory(Category category);

    List<Product> findAllById(List<Long> ids);
}
