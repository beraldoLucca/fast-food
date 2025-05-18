package br.com.fiap.fast_food.src.usecases;

import br.com.fiap.fast_food.src.adapters.IProductAdapter;
import br.com.fiap.fast_food.src.db.models.Customer;
import br.com.fiap.fast_food.src.db.models.Product;
import br.com.fiap.fast_food.src.dtos.CustomerRequest;
import br.com.fiap.fast_food.src.dtos.ProductRequest;
import br.com.fiap.fast_food.src.entities.CustomerEntity;
import br.com.fiap.fast_food.src.entities.ProductEntity;
import br.com.fiap.fast_food.src.enums.Category;
import br.com.fiap.fast_food.src.gateways.IProductGateway;
import br.com.fiap.fast_food.src.usecases.impl.ProductUsecaseImpl;
import br.com.fiap.fast_food.src.vo.Cpf;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class ProductUsecaseImplTest {

    private IProductUsecase productUsecase;

    @Mock
    private IProductAdapter productAdapter;

    @Mock
    private IProductGateway productGateway;

    AutoCloseable mock;

    @BeforeEach
    void setup(){
        mock = MockitoAnnotations.openMocks(this);
        productUsecase = new ProductUsecaseImpl(productAdapter);
    }

    @AfterEach
    void tearDown() throws Exception {
        mock.close();
    }

    @Test
    void shouldAllowSaveCustomerSuccessfully() {
        // Arrange
        var request = new ProductRequest("Chocolate", 4, BigDecimal.valueOf(3.50),
                "doce feito com chocolate meio amargo", "chocolate.png");
        var category = Category.fromValue(request.categoryId());
        var entity = new ProductEntity(request.name(), category, request.price(),
                request.description(), request.image());
        var product = new Product(1L, entity.getName(), entity.getCategory(), entity.getPrice(),
                entity.getDescription(), entity.getImage());

        when(productAdapter.toEntity(any(ProductEntity.class))).thenReturn(product);
        doNothing().when(productGateway).save(any(Product.class));

        //Act
        productUsecase.save(request, productGateway);

        // Assert
        verify(productAdapter, times(1)).toEntity(any(ProductEntity.class));
        verify(productGateway, times(1)).save(any(Product.class));
    }

    @Test
    void shouldAllowUpdateCustomerSuccessfully() {
        // Arrange
        var request = new ProductRequest("Chocolate", 4, BigDecimal.valueOf(3.50),
                "doce feito com chocolate meio amargo", "chocolate.png");
        var category = Category.fromValue(request.categoryId());
        var entity = new ProductEntity(request.name(), category, request.price(),
                request.description(), request.image());
        var product = new Product(1L, entity.getName(), entity.getCategory(), entity.getPrice(),
                entity.getDescription(), entity.getImage());

        when(productAdapter.toEntity(any(ProductEntity.class))).thenReturn(product);
        doNothing().when(productGateway).save(any(Product.class));

        //Act
        productUsecase.save(request, productGateway);

        // Assert
        verify(productAdapter, times(1)).toEntity(any(ProductEntity.class));
        verify(productGateway, times(1)).save(any(Product.class));
    }
}
