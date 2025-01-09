package br.com.fiap.fast_food.src.db.models;

import br.com.fiap.fast_food.src.vo.Cpf;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Customer {

    @EmbeddedId
    private Cpf cpf;

    @Column
    private String name;

    @Column
    private String email;


}
