package br.com.fiap.fast_food.src.usecases.impl;

import br.com.fiap.fast_food.src.adapters.IProductAdapter;
import br.com.fiap.fast_food.src.db.models.Product;
import br.com.fiap.fast_food.src.dtos.ProductRequest;
import br.com.fiap.fast_food.src.entities.ProductEntity;
import br.com.fiap.fast_food.src.enums.Category;
import br.com.fiap.fast_food.src.exceptions.ValidationException;
import br.com.fiap.fast_food.src.gateways.IDemandGateway;
import br.com.fiap.fast_food.src.gateways.IProductGateway;
import br.com.fiap.fast_food.src.usecases.IProductUsecase;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ProductUsecaseImpl implements IProductUsecase {

    private final IProductAdapter productAdapter;

    public ProductUsecaseImpl(IProductAdapter productAdapter) {
        this.productAdapter = productAdapter;
    }

    @Override
    @Transactional
    public void save(ProductRequest productRequest, IProductGateway gateway) {
        var entity = getProductEntity(productRequest);
        var product = productAdapter.toEntity(entity);
        gateway.save(product);
    }

    @Override
    @Transactional
    public void update(int id, ProductRequest productRequest, IProductGateway gateway) {
        var productFound = findById(id, gateway);
        if(productFound.isEmpty()) {
            throw new ValidationException("Produto não encontrado.");
        }else{
            var entity = getProductEntity(productRequest);
            var product = productAdapter.updateToEntity(entity, (long) id);
            gateway.save(product);
        }
    }

    @Override
    @Transactional
    public void remove(int id, IProductGateway productGateway, IDemandGateway demandGateway) {
        var product = validateProductExists(id, productGateway);
        validateProductInDemand(product, demandGateway);
        productGateway.remove(product);
    }

    @Override
    public List<Product> findByCategory(int categoryId, IProductGateway gateway) {
        var category = Category.fromValue(categoryId);
        return gateway.findByCategory(category);
    }

    @Override
    public List<Product> findAllById(List<Long> ids, IProductGateway gateway) {
        return gateway.findAllById(ids);
    }

    @Override
    public void validateProduct(List<Long> productIds) {
        if(productIds == null || productIds.isEmpty()) {
            throw new ValidationException("Por favor, selecione pelo menos um produto.");
        }
    }

    private Optional<Product> findById(long id, IProductGateway gateway) {
        return gateway.findById(id);
    }

    private ProductEntity getProductEntity(ProductRequest productRequest) {
        var category = Category.fromValue(productRequest.categoryId());
        return new ProductEntity(productRequest.name(), category, productRequest.price(), productRequest.description(), productRequest.image());
    }

    private void validateProductInDemand(Product product, IDemandGateway gateway) {
        var demand = gateway.findDemandByProducts(List.of(product));
        if(!demand.isEmpty()){
            throw new ValidationException("A exlusão não pode ser concluída pois já existe um pedido atrelado a esse produto.");
        }
    }

    private Product validateProductExists(int id, IProductGateway gateway) {
        var productFound = findById(id, gateway);
        if(productFound.isEmpty()) {
            throw new ValidationException("Produto não encontrado.");
        }
        return productFound.get();
    }
}
