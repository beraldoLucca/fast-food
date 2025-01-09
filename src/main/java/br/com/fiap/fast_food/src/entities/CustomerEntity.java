package br.com.fiap.fast_food.src.entities;

import br.com.fiap.fast_food.src.exceptions.ValidationException;
import br.com.fiap.fast_food.src.vo.Cpf;
import lombok.Getter;

@Getter
public class CustomerEntity {

    private Cpf cpf;

    private String name;

    private String email;

    public CustomerEntity(Cpf cpf, String name, String email) {
        if (name == null || name.isEmpty()) {
            throw new ValidationException("Nome não pode ser nulo ou vazio");
        }
        if(email == null || email.isEmpty()) {
            throw new ValidationException("Email não pode ser nulo ou vazio");
        }
        this.cpf = cpf;
        this.name = name;
        this.email = email;
    }
}
