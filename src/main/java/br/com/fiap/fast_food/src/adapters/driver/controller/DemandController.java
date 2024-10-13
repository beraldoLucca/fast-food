package br.com.fiap.fast_food.src.adapters.driver.controller;

import br.com.fiap.fast_food.src.adapters.driver.dto.CustomerRequest;
import br.com.fiap.fast_food.src.adapters.driver.dto.DemandRequest;
import br.com.fiap.fast_food.src.adapters.driver.dto.DemandResponse;
import br.com.fiap.fast_food.src.core.application.usecases.IDemandUsecase;
import br.com.fiap.fast_food.src.core.domain.entities.Demand;
import br.com.fiap.fast_food.src.core.domain.enums.DemandStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@Slf4j
public class DemandController {

    private final IDemandUsecase demandUsecase;

    public DemandController(IDemandUsecase demandUsecase) {
        this.demandUsecase = demandUsecase;
    }

    @PostMapping("/demand")
    @Operation(summary = "Solicitando um pedido.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido solicitado com sucesso",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DemandRequest.class)) }),
            @ApiResponse(responseCode = "400", description = "CPF não preenchido",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Nenhum produto selecionado",
                    content = @Content)})
    public ResponseEntity<String> saveDemand(@RequestBody DemandRequest demandRequest) {
        log.info("Solicitando um pedido: {}", demandRequest);
        demandRequest.setStatus(DemandStatus.RECEBIDO);
        demandUsecase.save(demandRequest);
        return ResponseEntity.ok().body("Pedido solicitado com sucesso!");
    }

    @PutMapping("/demand/{id}")
    @Operation(summary = "Finalização do pedido/Fake checkout")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido finalizado com sucesso",
                    content = { @Content(mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", description = "Pedido não encontrado",
                    content = @Content)})
    public ResponseEntity<String> finalizeDemand(@PathVariable("id") Long id) {
        log.info("Finalizando um pedido {}", id);
        demandUsecase.finalizeDemand(id);
        return ResponseEntity.ok().body("Pedido finalizado com sucesso!");
    }

    @GetMapping("/demands")
    @Operation(summary = "Listando todos os pedidos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido listados com sucesso",
                    content = { @Content(mediaType = "application/json") })})
    public ResponseEntity<List<DemandResponse>> getAllDemands(){
        log.info("Listando todos os pedidos.");
        return ResponseEntity.ok(demandUsecase.getAll());
    }
}
