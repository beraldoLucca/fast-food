package br.com.fiap.fast_food.src;

import br.com.fiap.fast_food.src.db.models.Customer;
import br.com.fiap.fast_food.src.db.models.Demand;
import br.com.fiap.fast_food.src.db.models.Product;
import br.com.fiap.fast_food.src.dtos.CustomerRequest;
import br.com.fiap.fast_food.src.dtos.DemandRequest;
import br.com.fiap.fast_food.src.enums.Category;
import br.com.fiap.fast_food.src.enums.DemandStatus;
import br.com.fiap.fast_food.src.enums.PaymentStatus;
import br.com.fiap.fast_food.src.vo.Cpf;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Utils {

    public static Customer createCustomer(){
        Cpf cpf = new Cpf("12345678910");
        var customer = new Customer();
        customer.setCpf(cpf);
        customer.setName("John Doe");
        customer.setEmail("john.doe@gmail.com");
        return customer;
    }

    public static Product createProduct(){
        Product product = new Product();
        product.setId(1L);
        product.setName("Coca");
        product.setCategory(Category.BEBIDA);
        product.setDescription("Bebida gaseificada");
        product.setPrice(BigDecimal.valueOf(6.50));
        product.setImage("coca.jpg");
        return product;
    }

    public static DemandRequest createDemandRequest(){
        CustomerRequest customerRequest = new CustomerRequest("12345678910", "John Doe", "john.doe@gmail.com");
        List<Long> longList = List.of(1L, 2L);
        DemandRequest demandRequest = new DemandRequest();
        demandRequest.setCustomer(customerRequest);
        demandRequest.setProductsId(longList);
        return demandRequest;
    }

    public static List<Demand> createDemandList(){
        List<Demand> demandList = new ArrayList<>();
        List<Product> productList = List.of(createProduct());
        Demand demand1 = new Demand();
        demand1.setId(1L);
        demand1.setCustomer(createCustomer());
        demand1.setProducts(productList);
        demand1.setCreatedAt(LocalTime.MIDNIGHT);
        demand1.setStatus(DemandStatus.RECEBIDO);
        demand1.setPaymentStatus(PaymentStatus.EM_ANDAMENTO);
        demand1.setPreparationTime(10.0);
        demandList.add(demand1);
        return demandList;
    }
}
