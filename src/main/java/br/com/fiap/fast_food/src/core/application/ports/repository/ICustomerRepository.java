package br.com.fiap.fast_food.src.core.application.ports.repository;

import br.com.fiap.fast_food.src.core.domain.entities.Customer;
import br.com.fiap.fast_food.src.core.domain.vo.Cpf;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICustomerRepository extends JpaRepository<Customer, Cpf> {
}
