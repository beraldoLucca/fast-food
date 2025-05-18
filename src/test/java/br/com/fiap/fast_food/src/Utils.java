package br.com.fiap.fast_food.src;

import br.com.fiap.fast_food.src.db.models.Customer;
import br.com.fiap.fast_food.src.vo.Cpf;

public class Utils {

    public static Customer createCustomer(){
        Cpf cpf = new Cpf("12345678910");
        var customer = new Customer();
        customer.setCpf(cpf);
        customer.setName("John Doe");
        customer.setEmail("john.doe@gmail.com");
        return customer;
    }
}
