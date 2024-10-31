package br.com.fiap.fast_food.src.adapters.driver.controller;

import br.com.fiap.fast_food.src.adapters.driver.dto.DemandRequest;
import br.com.fiap.fast_food.src.adapters.driver.dto.ProductRequest;
import br.com.fiap.fast_food.src.core.application.usecases.IProductUsecase;
import br.com.fiap.fast_food.src.core.domain.entities.Product;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@Validated
@Slf4j
public class ProductController {

    private final IProductUsecase productUsecase;

    public ProductController(IProductUsecase productUsecase) {
        this.productUsecase = productUsecase;
    }

    @PostMapping("/product")
    @Operation(summary = "Cadastrando novo produto.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto cadastrado com sucesso",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProductRequest.class)) }),
            @ApiResponse(responseCode = "400", description = "Categoria inválida",
                    content = @Content(mediaType = "text/plain"))})
    public ResponseEntity<String> saveProduct(@Valid @RequestBody ProductRequest product) {
        log.info("Cadastrando novo produto: {}", product);
        productUsecase.save(product);
        return ResponseEntity.ok("Produto cadastrado com sucesso!");
    }

    @PutMapping("/product/{id}")
    @Operation(summary = "Atualizando produto.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto atualizado com sucesso",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProductRequest.class)) }),
            @ApiResponse(responseCode = "400", description = "Produto não encontrado",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Categoria inválida",
                    content = @Content)})
    public ResponseEntity<String> updateProduct(@RequestBody ProductRequest product,
                                                @PathVariable Integer id) {
        log.info("Atualizando novo produto: {}, id: {}", product, id);
        productUsecase.update(id, product);
        return ResponseEntity.ok("Produto atualizado com sucesso!");
    }

    @DeleteMapping("/product/{id}")
    @Operation(summary = "Removendo produto.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto excluido com sucesso",
                    content = { @Content(mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", description = "Produto não encontrado",
                    content = @Content)})
    public ResponseEntity<String> removeProduct(@PathVariable Integer id) {
        log.info("Removendo produto, id: {}", id);
        productUsecase.remove(id);
        return ResponseEntity.ok("Produto excluido com sucesso!");
    }

    @GetMapping("/product/category/{id}")
    @Operation(summary = "Buscando produto por categoria.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto encontrado com sucesso",
                    content = { @Content(mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", description = "Categoria inválida",
                    content = @Content)})
    public ResponseEntity<List<Product>> findProductByCategory(@PathVariable Integer id) {
        log.info("Buscando produto, id da categoria: {}", id);
        var productList = productUsecase.findByCategory(id);
        return ResponseEntity.ok(productList);
    }
}
