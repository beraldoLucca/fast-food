package br.com.fiap.fast_food.src.gateways.impl;

import br.com.fiap.fast_food.src.db.models.Product;
import br.com.fiap.fast_food.src.enums.Category;
import br.com.fiap.fast_food.src.gateways.IProductGateway;
import br.com.fiap.fast_food.src.repositories.IProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductGatewayImpl implements IProductGateway {

    private final IProductRepository productRepository;

    public ProductGatewayImpl(IProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void save(Product product) {
        productRepository.save(product);
    }

    @Override
    public void remove(Product product) {
        productRepository.delete(product);
    }

    @Override
    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public List<Product> findByCategory(Category category) {
        return productRepository.findByCategory(category);
    }

    @Override
    public List<Product> findAllById(List<Long> ids) {
        return productRepository.findAllById(ids);
    }
}
