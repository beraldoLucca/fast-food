package br.com.fiap.fast_food.src.repositories;

import br.com.fiap.fast_food.src.db.models.Customer;
import br.com.fiap.fast_food.src.vo.Cpf;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICustomerRepository extends JpaRepository<Customer, Cpf> {
}
