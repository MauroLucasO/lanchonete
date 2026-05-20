package br.edu.ifsudestemg.lanchonete.api.controller;

import br.edu.ifsudestemg.lanchonete.api.dto.ItemPedidoDto;
import br.edu.ifsudestemg.lanchonete.exception.RegraNegocioException;
import br.edu.ifsudestemg.lanchonete.model.entity.ItemPedido;
import br.edu.ifsudestemg.lanchonete.service.ItemPedidoService;
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
@RequestMapping("/api/v1/itenspedidos")
@RequiredArgsConstructor
@Api("API de Itens")
@CrossOrigin
public class ItemPedidoController {

    private final ItemPedidoService service;

    @GetMapping()
    @ApiOperation("Obter detalhes de todos os itens")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Itens encontrados"),
            @ApiResponse(code = 404, message = "Itens não encontrados")
    })
    public ResponseEntity get() {
        List<ItemPedido> itemPedido = service.getItensPedido();
        return ResponseEntity.ok(itemPedido.stream().map(ItemPedidoDto::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @ApiOperation("Obter detalhes de um item")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Item encontrado"),
            @ApiResponse(code = 404, message = "Item não encontrado")
    })
    public ResponseEntity get(@PathVariable("id") @ApiParam("Id do item") Long id) {
        Optional<ItemPedido> itemPedido = service.getItemPedidoById(id);
        if (!itemPedido.isPresent()) {
            return new ResponseEntity("Item não encontrado", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(itemPedido.map(ItemPedidoDto::create));
    }

    @PostMapping()
    @ApiOperation("Inserir novo item")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Item inserido"),
            @ApiResponse(code = 404, message = "Item não inserido")
    })
    public ResponseEntity post(@RequestBody ItemPedidoDto dto) {
        try {
            ItemPedido itemPedido = converter(dto);
            itemPedido = service.salvar(itemPedido);
            return new ResponseEntity(itemPedido, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    @ApiOperation("Atualizar detalhes de um item")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Item atualizado"),
            @ApiResponse(code = 404, message = "Item não encontrado")
    })
    public ResponseEntity put(@PathVariable("id") @ApiParam("Id do item") Long id, @RequestBody ItemPedidoDto dto) {
        if (!service.getItemPedidoById(id).isPresent()) {
            return new ResponseEntity("Item não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            ItemPedido itemPedido = converter(dto);
            itemPedido.setId(id);
            service.salvar(itemPedido);
            return ResponseEntity.ok(itemPedido);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    @ApiOperation("Excluir um item")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Item excluído"),
            @ApiResponse(code = 404, message = "Item não encontrado")
    })
    public ResponseEntity excluir(@PathVariable("id") @ApiParam("Id do item") Long id) {

        Optional<ItemPedido> itemPedido = service.getItemPedidoById(id);

        if (!itemPedido.isPresent()) {
            return new ResponseEntity("Item não encontrado", HttpStatus.NOT_FOUND);
        }

        try {
            service.excluir(itemPedido.get());
            return new ResponseEntity(HttpStatus.NO_CONTENT);

        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public ItemPedido converter(ItemPedidoDto dto) {
        ModelMapper modelMapper = new ModelMapper();
        ItemPedido itemPedido = modelMapper.map(dto, ItemPedido.class);
        return itemPedido;
    }
}
