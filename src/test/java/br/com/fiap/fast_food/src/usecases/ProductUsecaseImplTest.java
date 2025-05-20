package br.com.fiap.fast_food.src.usecases;

import br.com.fiap.fast_food.src.Utils;
import br.com.fiap.fast_food.src.adapters.IProductAdapter;
import br.com.fiap.fast_food.src.db.models.Customer;
import br.com.fiap.fast_food.src.db.models.Demand;
import br.com.fiap.fast_food.src.db.models.Product;
import br.com.fiap.fast_food.src.dtos.CustomerRequest;
import br.com.fiap.fast_food.src.dtos.ProductRequest;
import br.com.fiap.fast_food.src.entities.CustomerEntity;
import br.com.fiap.fast_food.src.entities.ProductEntity;
import br.com.fiap.fast_food.src.enums.Category;
import br.com.fiap.fast_food.src.exceptions.ValidationException;
import br.com.fiap.fast_food.src.gateways.IDemandGateway;
import br.com.fiap.fast_food.src.gateways.IProductGateway;
import br.com.fiap.fast_food.src.usecases.impl.ProductUsecaseImpl;
import br.com.fiap.fast_food.src.vo.Cpf;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class ProductUsecaseImplTest {

    private IProductUsecase productUsecase;

    @Mock
    private IProductAdapter productAdapter;

    @Mock
    private IProductGateway productGateway;

    @Mock
    private IDemandGateway demandGateway;

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
    void shouldAllowSaveProductSuccessfully() {
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
    void shouldAllowUpdateProductSuccessfully() {
        // Arrange
        int productId = 1;
        var request = new ProductRequest("Chocolate", 4, BigDecimal.valueOf(3.50),
                "doce feito com chocolate meio amargo", "chocolate.png");
        var productExistent = Utils.createProduct();
        var productUpdated = new Product(1L, request.name(), Category.fromValue(request.categoryId()),
                request.price(), request.description(), request.image());

        when(productGateway.findById((long) productId)).thenReturn(Optional.of(productExistent));
        when(productAdapter.updateToEntity(any(ProductEntity.class), eq(1L))).thenReturn(productUpdated);
        // Act
        productUsecase.update(1, request, productGateway);

        // Assert
        verify(productGateway, times(1)).save(productUpdated);
    }

    @Test
    void shouldThrowExceptionWhenProductNotFound() {
        // Arrange
        var request = new ProductRequest("Chocolate", 4, BigDecimal.valueOf(3.50),
                "doce feito com chocolate meio amargo", "chocolate.png");
        when(productGateway.findById(1L)).thenReturn(Optional.empty());

        // Act and Assert
        assertThatThrownBy(() -> productUsecase.update(1, request, productGateway))
                .isInstanceOf(ValidationException.class)
                .hasMessage("Produto não encontrado.");
    }

    @Test
    void shouldAllowRemoveProductSuccessfully() {
        // Arrange
        int productId = 1;
        var productExistent = Utils.createProduct();
        List<Demand> demands = new ArrayList<>();

        when(productGateway.findById((long) productId)).thenReturn(Optional.of(productExistent));
        when(demandGateway.findDemandByProducts(List.of(productExistent))).thenReturn(demands);
        // Act
        productUsecase.remove(productId, productGateway, demandGateway);

        // Assert
        verify(productGateway, times(1)).remove(productExistent);
    }

    @Test
    void shouldThrowExceptionWhenRemoveEmptyProductList() {
        // Arrange
        int productId = 1;

        when(productGateway.findById((long) productId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThatThrownBy(() -> productUsecase.remove(productId, productGateway, demandGateway))
                .isInstanceOf(ValidationException.class)
                .hasMessage("Produto não encontrado.");
    }

    @Test
    void shouldThrowExceptionProductInDemand() {
        // Arrange
        int productId = 1;
        var productExistent = Utils.createProduct();
        Demand demand = new Demand();
        demand.setProducts(List.of(productExistent));
        List<Demand> demands = new ArrayList<>();
        demands.add(demand);

        when(productGateway.findById((long) productId)).thenReturn(Optional.of(productExistent));
        when(demandGateway.findDemandByProducts(List.of(productExistent))).thenReturn(demands);
        // Act and Assert
        assertThatThrownBy(() -> productUsecase.remove(1, productGateway, demandGateway))
                .isInstanceOf(ValidationException.class)
                .hasMessage("A exlusão não pode ser concluída pois já existe um pedido atrelado a esse produto.");
    }

    @Test
    void shouldAllowFindProductByCategorySuccessfully() {
        // Arrange
        var product = Utils.createProduct();
        when(productGateway.findByCategory(any(Category.class))).thenReturn(List.of(product));

        // Act
        var products = productUsecase.findByCategory(4, productGateway);

        // Assert
        Assertions.assertEquals(1, products.size());
    }

    @Test
    void shouldAllowFindAllProductByIdSuccessfully() {
        // Arrange
        var id = 1L;
        var idList = List.of(id);
        var product = Utils.createProduct();
        when(productGateway.findAllById(idList)).thenReturn(List.of(product));

        // Act
        var products = productUsecase.findAllById(idList, productGateway);

        // Assert
        Assertions.assertEquals(1, products.size());
    }

    @Test
    void shouldAllowValidateProductWithNullValuesSuccessfully() {
        // Arrange
        List<Long> longList = null;

        // Act and Assert
        assertThatThrownBy(() -> productUsecase.validateProduct(longList))
                .isInstanceOf(ValidationException.class)
                .hasMessage("Por favor, selecione pelo menos um produto.");
    }

    @Test
    void shouldAllowValidateProductWithEmptyValuesSuccessfully() {
        // Arrange
        List<Long> longList = new ArrayList<>();

        // Act and Assert
        assertThatThrownBy(() -> productUsecase.validateProduct(longList))
                .isInstanceOf(ValidationException.class)
                .hasMessage("Por favor, selecione pelo menos um produto.");
    }

    @Test
    void shouldAllowValidateProductWhenIdsHasMultipleValues() {
        // Arrange
        List<Long> validIds = List.of(1L, 2L, 3L);

        // Act and Assert
        assertDoesNotThrow(() -> productUsecase.validateProduct(validIds));
    }
}
