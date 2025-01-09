package br.com.fiap.fast_food.src.entities;

import br.com.fiap.fast_food.src.enums.Category;
import br.com.fiap.fast_food.src.exceptions.ValidationException;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class ProductEntity {

    private String name;

    private Category category;

    private BigDecimal price;

    private String description;

    private String image;

    public ProductEntity(String name, Category category, BigDecimal price, String description, String image) {
        if (name.isEmpty()){
            throw new ValidationException("Nome não pode ser vazio");
        }
        if (price.compareTo(new BigDecimal(0)) < 0){
            throw new ValidationException("Preço não pode ser menor que zero");
        }
        if (description.isEmpty()){
            throw new ValidationException("Descrição não pode ser vazia");
        }
        this.name = name;
        this.category = category;
        this.price = price;
        this.description = description;
        this.image = image;
    }
}
