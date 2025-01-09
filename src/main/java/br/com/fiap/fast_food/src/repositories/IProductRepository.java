package br.com.fiap.fast_food.src.repositories;

import br.com.fiap.fast_food.src.db.models.Product;
import br.com.fiap.fast_food.src.enums.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByCategory(Category category);
}
