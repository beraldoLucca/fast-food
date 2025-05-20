package br.com.fiap.fast_food.src.gateway;

import br.com.fiap.fast_food.src.Utils;
import br.com.fiap.fast_food.src.enums.Category;
import br.com.fiap.fast_food.src.gateways.impl.ProductGatewayImpl;
import br.com.fiap.fast_food.src.repositories.IProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

public class ProductGatewayImplTest {

    private ProductGatewayImpl productGateway;

    @Mock
    private IProductRepository productRepository;

    AutoCloseable mock;

    @BeforeEach
    void setup(){
        mock = MockitoAnnotations.openMocks(this);
        productGateway = new ProductGatewayImpl(productRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        mock.close();
    }

    @Test
    void shouldAllowSaveProduct(){
        var product = Utils.createProduct();
        productRepository.save(product);

        productGateway.save(product);
    }

    @Test
    void shouldAllowRemoveProduct(){
        var product = Utils.createProduct();
        productRepository.delete(product);

        productGateway.remove(product);
    }

    @Test
    void shouldAllowFindProductById(){
        var product = Utils.createProduct();
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        var productReturned = productGateway.findById(1L).get();

        Assertions.assertEquals(product.getName(), productReturned.getName());
        Assertions.assertEquals(product.getId(), productReturned.getId());
        Assertions.assertEquals(product.getCategory(), productReturned.getCategory());
    }

    @Test
    void shouldAllowFindProductByCategory(){
        var product = Utils.createProduct();
        var productList = List.of(product);
        when(productRepository.findByCategory(Category.BEBIDA)).thenReturn(productList);

        var productReturnedList = productGateway.findByCategory(Category.BEBIDA);

        Assertions.assertEquals(1, productReturnedList.size());
    }

    @Test
    void shouldAllowFindAllProductById(){
        var product = Utils.createProduct();
        var productList = List.of(product);
        var longList = List.of(1L, 2L, 3L);
        when(productRepository.findAllById(longList)).thenReturn(productList);

        var productReturnedList = productGateway.findAllById(longList);

        Assertions.assertEquals(1, productReturnedList.size());
    }
}
