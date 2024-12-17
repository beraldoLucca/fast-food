package br.com.fiap.fast_food.src.core.application.ports.repository;

import br.com.fiap.fast_food.src.core.domain.entities.Demand;
import br.com.fiap.fast_food.src.core.domain.enums.DemandStatus;
import br.com.fiap.fast_food.src.core.domain.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface IDemandRepository extends JpaRepository<Demand, Long> {

    @Modifying
    @Transactional
    @Query("UPDATE Demand d SET d.paymentStatus = :paymentStatus WHERE d.id = :id")
    void updatePaymentStatus(@Param("id") Long id, @Param("paymentStatus") PaymentStatus paymentStatus);

    @Query("SELECT d FROM Demand d " +
            "WHERE d.status != :statusFinalizado " +
            "ORDER BY " +
            "   CASE d.status " +
            "       WHEN :statusPronto THEN 1 " +
            "       WHEN :statusEmPreparacao THEN 2 " +
            "       WHEN :statusRecebido THEN 3 " +
            "   END, " +
            "d.createdAt ASC")
    List<Demand> findAllDemandsInOrder(@Param("statusFinalizado") DemandStatus statusFinalizado,
                                       @Param("statusPronto") DemandStatus statusPronto,
                                       @Param("statusEmPreparacao") DemandStatus statusEmPreparacao,
                                       @Param("statusRecebido") DemandStatus statusRecebido);
}
