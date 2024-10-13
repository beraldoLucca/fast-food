package br.com.fiap.fast_food.src.core.application.ports.repository;

import br.com.fiap.fast_food.src.core.domain.entities.Product;
import br.com.fiap.fast_food.src.core.domain.enums.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByCategory(Category category);
}
