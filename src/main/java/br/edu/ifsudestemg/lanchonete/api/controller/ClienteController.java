package br.edu.ifsudestemg.lanchonete.api.controller;

import br.edu.ifsudestemg.lanchonete.api.dto.CategoriaDto;
import br.edu.ifsudestemg.lanchonete.api.dto.ClienteDto;
import br.edu.ifsudestemg.lanchonete.exception.RegraNegocioException;
import br.edu.ifsudestemg.lanchonete.model.entity.Categoria;
import br.edu.ifsudestemg.lanchonete.model.entity.Cliente;
import br.edu.ifsudestemg.lanchonete.service.ClienteService;
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
@RequestMapping("/api/v1/clientes")
@RequiredArgsConstructor
@Api("API de Clientes")
@CrossOrigin
public class ClienteController {

    private final ClienteService service;

    @GetMapping()
    @ApiOperation("Obter detalhes de todos os clientes")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Clientes encontrados"),
            @ApiResponse(code = 404, message = "Clientes não encontrados")
    })
    public ResponseEntity get() {
        List<Cliente> clientes = service.getClientes();
        return ResponseEntity.ok(clientes.stream().map(ClienteDto::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @ApiOperation("Obter detalhes de um cliente")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Cliente encontrado"),
            @ApiResponse(code = 404, message = "Cliente não encontrado")
    })
    public ResponseEntity get(@PathVariable("id") @ApiParam("Id do cliente") Long id) {
        Optional<Cliente> cliente = service.getClienteById(id);
        if (!cliente.isPresent()) {
            return new ResponseEntity("Cliente não encontrado", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(cliente.map(ClienteDto::create));
    }

    @PostMapping()
    @ApiOperation("Inserir novo cliente")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Cliente inserido"),
            @ApiResponse(code = 404, message = "Cliente não inserido")
    })
    public ResponseEntity post(@RequestBody ClienteDto dto) {
        try {
            Cliente cliente = converter(dto);
            cliente = service.salvar(cliente);
            return new ResponseEntity(cliente, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    @ApiOperation("Atualizar detalhes de um cliente")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Cliente atualizado"),
            @ApiResponse(code = 404, message = "Cliente não encontrado")
    })
    public ResponseEntity put(@PathVariable("id") @ApiParam("Id do cliente") Long id, @RequestBody ClienteDto dto) {
        if (!service.getClienteById(id).isPresent()) {
            return new ResponseEntity("Cliente não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            Cliente cliente = converter(dto);
            cliente.setId(id);
            service.salvar(cliente);
            return ResponseEntity.ok(cliente);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    @ApiOperation("Excluir um cliente")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Cliente excluído"),
            @ApiResponse(code = 404, message = "Cliente não encontrado")
    })
    public ResponseEntity excluir(@PathVariable("id") @ApiParam("Id do cliente") Long id) {

        Optional<Cliente> cliente = service.getClienteById(id);

        if (!cliente.isPresent()) {
            return new ResponseEntity("Cliente não encontrado", HttpStatus.NOT_FOUND);
        }

        try {
            service.excluir(cliente.get());
            return new ResponseEntity(HttpStatus.NO_CONTENT);

        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public Cliente converter(ClienteDto dto) {
        ModelMapper modelMapper = new ModelMapper();
        Cliente cliente = modelMapper.map(dto, Cliente.class);
        return cliente;
    }
}