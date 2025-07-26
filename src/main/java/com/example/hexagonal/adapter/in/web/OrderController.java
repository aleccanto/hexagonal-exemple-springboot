package com.example.hexagonal.adapter.in.web;

import com.example.hexagonal.adapter.in.web.dto.OrderDTO;
import com.example.hexagonal.adapter.in.web.dto.OrderDTOMapper;
import com.example.hexagonal.domain.model.Order;
import com.example.hexagonal.domain.port.in.OrderUseCase;
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
@RequestMapping("/orders")
public class OrderController {

    private final OrderUseCase orderUseCase;

    public OrderController(OrderUseCase orderUseCase) {
        this.orderUseCase = orderUseCase;
    }

    @Operation(summary = "Cria um novo pedido", description = "Cria um novo pedido com os dados fornecidos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pedido criado com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderDTO.class))),
            @ApiResponse(responseCode = "400", description = "Requisição inválida",
                    content = @Content)
    })
    @PostMapping
    public ResponseEntity<OrderDTO> createOrder(
            @Parameter(description = "Dados do pedido a ser criado", required = true) @RequestBody OrderDTO orderDTO) {
        Order order = OrderDTOMapper.toDomain(orderDTO);
        Order createdOrder = orderUseCase.createOrder(order);
        return new ResponseEntity<>(OrderDTOMapper.fromDomain(createdOrder), HttpStatus.CREATED);
    }

    @Operation(summary = "Obtém um pedido pelo ID", description = "Retorna um pedido específico com base no ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido encontrado com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderDTO.class))),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado",
                    content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOrderById(
            @Parameter(description = "ID do pedido a ser obtido", required = true) @PathVariable Long id) {
        Optional<Order> order = orderUseCase.getOrderById(id);
        return order.map(value -> new ResponseEntity<>(OrderDTOMapper.fromDomain(value), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(summary = "Obtém todos os pedidos", description = "Retorna uma lista de todos os pedidos disponíveis.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de pedidos obtida com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderDTO.class)))
    })
    @GetMapping
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        List<Order> orders = orderUseCase.getAllOrders();
        return new ResponseEntity<>(OrderDTOMapper.fromDomain(orders), HttpStatus.OK);
    }

    @Operation(summary = "Atualiza um pedido existente", description = "Atualiza os dados de um pedido existente com base no ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido atualizado com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderDTO.class))),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Requisição inválida",
                    content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<OrderDTO> updateOrder(
            @Parameter(description = "ID do pedido a ser atualizado", required = true) @PathVariable Long id,
            @Parameter(description = "Novos dados do pedido", required = true) @RequestBody OrderDTO orderDTO) {
        Order order = OrderDTOMapper.toDomain(orderDTO);
        Optional<Order> updatedOrder = orderUseCase.updateOrder(id, order);
        return updatedOrder.map(value -> new ResponseEntity<>(OrderDTOMapper.fromDomain(value), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(summary = "Exclui um pedido", description = "Exclui um pedido específico com base no ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Pedido excluído com sucesso",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado",
                    content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(
            @Parameter(description = "ID do pedido a ser excluído", required = true) @PathVariable Long id) {
        if (orderUseCase.deleteOrder(id)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}

