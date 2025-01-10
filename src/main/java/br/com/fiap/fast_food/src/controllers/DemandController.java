package br.com.fiap.fast_food.src.controllers;

import br.com.fiap.fast_food.src.dtos.DemandRequest;
import br.com.fiap.fast_food.src.dtos.DemandResponse;
import br.com.fiap.fast_food.src.gateways.impl.CustomerGatewayImpl;
import br.com.fiap.fast_food.src.gateways.impl.DemandGatewayImpl;
import br.com.fiap.fast_food.src.gateways.impl.ProductGatewayImpl;
import br.com.fiap.fast_food.src.repositories.ICustomerRepository;
import br.com.fiap.fast_food.src.repositories.IDemandRepository;
import br.com.fiap.fast_food.src.repositories.IProductRepository;
import br.com.fiap.fast_food.src.usecases.IDemandUsecase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@Slf4j
public class DemandController {

    private final IDemandUsecase demandUsecase;

    private final ICustomerRepository iCustomerRepository;

    private final IProductRepository iProductRepository;

    private final IDemandRepository iDemandRepository;

    public DemandController(IDemandUsecase demandUsecase, ICustomerRepository iCustomerRepository, IProductRepository iProductRepository, IDemandRepository iDemandRepository) {
        this.demandUsecase = demandUsecase;
        this.iCustomerRepository = iCustomerRepository;
        this.iProductRepository = iProductRepository;
        this.iDemandRepository = iDemandRepository;
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
        var customerGateway = new CustomerGatewayImpl(iCustomerRepository);
        var productGateway = new ProductGatewayImpl(iProductRepository);
        var demandGateway = new DemandGatewayImpl(iDemandRepository);
        demandUsecase.save(demandRequest, customerGateway, productGateway, demandGateway);
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
        var demandGateway = new DemandGatewayImpl(iDemandRepository);
        demandUsecase.finalizeDemand(id, demandGateway);
        return ResponseEntity.ok().body("Pedido: " + id + " finalizado com sucesso!");
    }

    @GetMapping("/demands")
    @Operation(summary = "Listando todos os pedidos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido listados com sucesso",
                    content = { @Content(mediaType = "application/json") })})
    public ResponseEntity<List<DemandResponse>> findAllDemands(){
        log.info("Listando todos os pedidos.");
        var demandGateway = new DemandGatewayImpl(iDemandRepository);
        return ResponseEntity.ok(demandUsecase.findAll(demandGateway));
    }

    @GetMapping("/demand/{id}/payment-status")
    @Operation(summary = "Consultando status de pagamento do pedido.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido listado com sucesso",
                    content = { @Content(mediaType = "application/json") })})
    public ResponseEntity<String> findDemandPaymentStatus(@PathVariable("id") Long id) {
        log.info("Verificando status de pagamento do pedido. {}", id);
        var demandGateway = new DemandGatewayImpl(iDemandRepository);
        var demand = demandUsecase.findDemandPaymentStatus(id, demandGateway);
        return ResponseEntity.ok().body("Pedido: " + id + ", status de pagamento: " + demand.getPaymentStatus());
    }

    @PostMapping("/demand/webhook/payment")
    @Operation(summary = "Webhook para confirmação de pagamento")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido com pagamento aprovado",
                    content = { @Content(mediaType = "application/json") })})
    public ResponseEntity<String> handleWebhook(@RequestBody Map<String, Object> payload,
                                                @RequestHeader(HttpHeaders.AUTHORIZATION) String signatureHeader) throws NoSuchAlgorithmException, InvalidKeyException {
        log.info("Pagando o pedido...");
        var demandGateway = new DemandGatewayImpl(iDemandRepository);
        demandUsecase.processPayment(payload, signatureHeader, demandGateway);
        return ResponseEntity.ok().body("Pagamento aprovado com sucesso.");
    }

    @GetMapping("/demand/{id}/generate-signature")
    @Operation(summary = "Geração do hash da assinatura")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Assinatura gerada com sucesso",
                    content = { @Content(mediaType = "application/json") })})
    public ResponseEntity<String> generateSignature(@PathVariable Integer id) throws NoSuchAlgorithmException, InvalidKeyException {
        log.info("Gerando a assinatura...");
        var signature = demandUsecase.getGeneratedSignature(id);
        return ResponseEntity.ok().body("Assinatura gerada: " + signature);
    }
}
