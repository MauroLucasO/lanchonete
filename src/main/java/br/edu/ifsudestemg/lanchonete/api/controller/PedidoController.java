package br.edu.ifsudestemg.lanchonete.api.controller;

import br.edu.ifsudestemg.lanchonete.api.dto.PedidoDto;
import br.edu.ifsudestemg.lanchonete.exception.RegraNegocioException;
import br.edu.ifsudestemg.lanchonete.model.entity.Pedido;
import br.edu.ifsudestemg.lanchonete.service.PedidoService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/pedidos")
@RequiredArgsConstructor
@Api("API de Pedidos")
@CrossOrigin
public class PedidoController {

    private final PedidoService service;

    @GetMapping()
    @ApiOperation("Obter detalhes de todos os pedidos")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Pedidos encontrados"),
            @ApiResponse(code = 404, message = "Pedidos não encontrados")
    })
    public ResponseEntity get() {
        List<Pedido> pedidos = service.getPedidos();
        return ResponseEntity.ok(pedidos.stream().map(PedidoDto::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @ApiOperation("Obter detalhes de um pedido")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Pedido encontrado"),
            @ApiResponse(code = 404, message = "Pedido não encontrado")
    })
    public ResponseEntity get(@PathVariable("id") @ApiParam("Id do pedido") Long id) {
        Optional<Pedido> pedido = service.getPedidoById(id);
        if (!pedido.isPresent()) {
            return new ResponseEntity("Pedido não encontrado", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(pedido.map(PedidoDto::create));
    }

    @PostMapping()
    @ApiOperation("Inserir novo pedido")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Pedido inserido"),
            @ApiResponse(code = 404, message = "Pedido não inserido")
    })
    public ResponseEntity post(@RequestBody PedidoDto dto) {
        try {
            Pedido pedido = converter(dto);
            pedido = service.salvar(pedido);
            return new ResponseEntity(pedido, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    @ApiOperation("Atualizar detalhes de um pedido")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Pedido atualizado"),
            @ApiResponse(code = 404, message = "Pedido não encontrado")
    })
    public ResponseEntity put(@PathVariable("id") @ApiParam("Id do pedido") Long id, @RequestBody PedidoDto dto) {
        if (!service.getPedidoById(id).isPresent()) {
            return new ResponseEntity("Pedido não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            Pedido pedido = converter(dto);
            pedido.setId(id);
            service.salvar(pedido);
            return ResponseEntity.ok(pedido);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    @ApiOperation("Excluir um pedido")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Pedido excluído"),
            @ApiResponse(code = 404, message = "Pedido não encontrado")
    })
    public ResponseEntity excluir(@PathVariable("id") @ApiParam("Id do pedido") Long id) {

        Optional<Pedido> pedido = service.getPedidoById(id);

        if (!pedido.isPresent()) {
            return new ResponseEntity("Pedido não encontrado", HttpStatus.NOT_FOUND);
        }

        try {
            service.excluir(pedido.get());
            return new ResponseEntity(HttpStatus.NO_CONTENT);

        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public Pedido converter(PedidoDto dto) {
        ModelMapper modelMapper = new ModelMapper();
        Pedido pedido = modelMapper.map(dto, Pedido.class);
        return pedido;
    }
}
