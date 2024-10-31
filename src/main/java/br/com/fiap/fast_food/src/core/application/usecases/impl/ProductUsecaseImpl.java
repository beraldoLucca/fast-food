package br.com.fiap.fast_food.src.core.application.usecases.impl;

import br.com.fiap.fast_food.src.adapters.driver.dto.ProductRequest;
import br.com.fiap.fast_food.src.core.application.mapper.IProductMapper;
import br.com.fiap.fast_food.src.core.application.ports.repository.IProductRepository;
import br.com.fiap.fast_food.src.core.application.usecases.IProductUsecase;
import br.com.fiap.fast_food.src.core.domain.entities.Product;
import br.com.fiap.fast_food.src.core.domain.enums.Category;
import br.com.fiap.fast_food.src.core.domain.exception.ValidationException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ProductUsecaseImpl implements IProductUsecase {

    private final IProductRepository productRepository;

    private final IProductMapper productMapper;

    public ProductUsecaseImpl(IProductRepository productRepository, IProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    @Override
    @Transactional
    public void save(ProductRequest productRequest) {
        var category = Category.fromValue(productRequest.categoryId());
        var product = productMapper.toEntity(productRequest, category);
        productRepository.save(product);
    }

    @Override
    @Transactional
    public void update(int id, ProductRequest productRequest) {
        var productFound = findById(id);
        if(productFound.isEmpty()) {
            throw new ValidationException("Produto não encontrado.");
        }else{
            var category = Category.fromValue(productRequest.categoryId());
            var product = productMapper.updateToEntity(productRequest, category, (long) id);
            productRepository.save(product);
        }
    }

    @Override
    @Transactional
    public void remove(int id) {
        var productFound = findById(id);
        if(productFound.isEmpty()) {
            throw new ValidationException("Produto não encontrado.");
        }
        else{
            productRepository.delete(productFound.get());
        }
    }

    @Override
    public List<Product> findByCategory(int categoryId) {
        var category = Category.fromValue(categoryId);
        return productRepository.findByCategory(category);
    }

    @Override
    public List<Product> findAllById(List<Long> ids) {
        return productRepository.findAllById(ids);
    }

    private Optional<Product> findById(long id) {
        return productRepository.findById(id);
    }
}
