package br.com.fiap.fast_food.src.core.application.ports.repository;

import br.com.fiap.fast_food.src.core.domain.entities.Demand;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IDemandRepository extends JpaRepository<Demand, Long> {
}
