package br.com.fiap.fast_food.src.adapters.driver.controller;

import br.com.fiap.fast_food.src.adapters.driver.dto.CustomerRequest;
import br.com.fiap.fast_food.src.core.application.usecases.ICustomerUsecase;
import br.com.fiap.fast_food.src.core.domain.entities.Customer;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@Slf4j
public class CustomerController {

    private final ICustomerUsecase customerUsecase;

    public CustomerController(ICustomerUsecase customerUsecase) {
        this.customerUsecase = customerUsecase;
    }

    @PostMapping("/customer")
    @Operation(summary = "Cadastrar novo cliente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente cadastrado com sucesso",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomerRequest.class)) }),
            @ApiResponse(responseCode = "400", description = "CPF inválido",
                    content = @Content)})
    public ResponseEntity<String> saveCustomer(@RequestBody CustomerRequest request) {
        log.info("Cadastrando novo cliente: {}", request);
        customerUsecase.save(request);
        return ResponseEntity.ok("Cliente cadastrado com sucesso!");
    }

    @GetMapping("/customer/{cpf}")
    @Operation(summary = "Identificar cliente a partir do CPF.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente validado com sucesso",
                    content = { @Content(mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", description = "Cliente não encontrado",
                    content = @Content)})
    public ResponseEntity<String> findCustomerByCpf(@PathVariable String cpf) {
        log.info("Busca do cliente com CPF: {}", cpf);
        customerUsecase.findByCpf(cpf);
        return ResponseEntity.ok("Cliente validado com sucesso!");
    }

    @GetMapping("/customer")
    @Operation(summary = "Listar todos os Clientes")
    public ResponseEntity<List<Customer>> findAll() {
        log.info("get all");
        return ResponseEntity.ok(customerUsecase.findAll());
    }
}
