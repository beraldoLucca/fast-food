package br.com.fiap.fast_food.src.usecases;

import br.com.fiap.fast_food.src.dtos.ProductRequest;
import br.com.fiap.fast_food.src.db.models.Product;
import br.com.fiap.fast_food.src.gateways.IProductGateway;

import java.util.List;

public interface IProductUsecase {

    void save(ProductRequest product, IProductGateway gateway);

    void update(int id, ProductRequest product, IProductGateway gateway);

    void remove(int id, IProductGateway gateway);

    List<Product> findByCategory(int categoryId, IProductGateway gateway);

    List<Product> findAllById(List<Long> ids, IProductGateway gateway);
}
