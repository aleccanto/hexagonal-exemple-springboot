package com.example.hexagonal.adapter.in.web;

import com.example.hexagonal.adapter.in.web.dto.ProductDTO;
import com.example.hexagonal.adapter.in.web.dto.ProductDTOMapper;
import com.example.hexagonal.domain.model.Product;
import com.example.hexagonal.domain.port.in.ProductUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductUseCase productUseCase;

    public ProductController(ProductUseCase productUseCase) {
        this.productUseCase = productUseCase;
    }

    @Operation(summary = "Cria um novo produto", description = "Cria um novo produto com os dados fornecidos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Produto criado com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductDTO.class))),
            @ApiResponse(responseCode = "400", description = "Requisição inválida",
                    content = @Content)
    })
    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(
            @Parameter(description = "Dados do produto a ser criado", required = true) @RequestBody ProductDTO productDTO) {
        Product product = ProductDTOMapper.toDomain(productDTO);
        Product createdProduct = productUseCase.createProduct(product);
        return new ResponseEntity<>(ProductDTOMapper.fromDomain(createdProduct), HttpStatus.CREATED);
    }

    @Operation(summary = "Obtém um produto pelo ID", description = "Retorna um produto específico com base no ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto encontrado com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductDTO.class))),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado",
                    content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(
            @Parameter(description = "ID do produto a ser obtido", required = true) @PathVariable Long id) {
        Optional<Product> product = productUseCase.getProductById(id);
        return product.map(value -> new ResponseEntity<>(ProductDTOMapper.fromDomain(value), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(summary = "Obtém todos os produtos", description = "Retorna uma lista de todos os produtos disponíveis.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de produtos obtida com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductDTO.class)))
    })
    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        List<Product> products = productUseCase.getAllProducts();
        return new ResponseEntity<>(ProductDTOMapper.fromDomain(products), HttpStatus.OK);
    }

    @Operation(summary = "Atualiza um produto existente", description = "Atualiza os dados de um produto existente com base no ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto atualizado com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductDTO.class))),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Requisição inválida",
                    content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(
            @Parameter(description = "ID do produto a ser atualizado", required = true) @PathVariable Long id,
            @Parameter(description = "Novos dados do produto", required = true) @RequestBody ProductDTO productDTO) {
        Product product = ProductDTOMapper.toDomain(productDTO);
        Optional<Product> updatedProduct = productUseCase.updateProduct(id, product);
        return updatedProduct.map(value -> new ResponseEntity<>(ProductDTOMapper.fromDomain(value), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(summary = "Exclui um produto", description = "Exclui um produto específico com base no ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Produto excluído com sucesso",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado",
                    content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(
            @Parameter(description = "ID do produto a ser excluído", required = true) @PathVariable Long id) {
        if (productUseCase.deleteProduct(id)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}

