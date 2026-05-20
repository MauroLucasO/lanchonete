package br.edu.ifsudestemg.lanchonete.api.controller;

import br.edu.ifsudestemg.lanchonete.api.dto.CategoriaDto;
import br.edu.ifsudestemg.lanchonete.api.dto.ProprietarioDto;
import br.edu.ifsudestemg.lanchonete.exception.RegraNegocioException;
import br.edu.ifsudestemg.lanchonete.model.entity.Categoria;
import br.edu.ifsudestemg.lanchonete.model.entity.Proprietario;
import br.edu.ifsudestemg.lanchonete.service.ProprietarioService;
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
@RequestMapping("/api/v1/proprietarios")
@RequiredArgsConstructor
@Api("API de Proprietários")
@CrossOrigin
public class ProprietarioController {

    private final ProprietarioService service;

    @GetMapping()
    @ApiOperation("Obter detalhes de todos os proprietários")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Proprietários encontrados"),
            @ApiResponse(code = 404, message = "Proprietários não encontrados")
    })
    public ResponseEntity get() {
        List<Proprietario> proprietarios = service.getProprietarios();
        return ResponseEntity.ok(proprietarios.stream().map(ProprietarioDto::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @ApiOperation("Obter detalhes de um proprietário")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Proprietário encontrado"),
            @ApiResponse(code = 404, message = "Proprietário não encontrado")
    })
    public ResponseEntity get(@PathVariable("id") @ApiParam("Id do proprietário") Long id) {
        Optional<Proprietario> proprietario = service.getProprietarioById(id);
        if (!proprietario.isPresent()) {
            return new ResponseEntity("Proprietário não encontrado", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(proprietario.map(ProprietarioDto::create));
    }

    @PostMapping()
    @ApiOperation("Inserir novo proprietário")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Proprietário inserido"),
            @ApiResponse(code = 404, message = "Proprietário não inserido")
    })
    public ResponseEntity post(@RequestBody ProprietarioDto dto) {
        try {
            Proprietario proprietario = converter(dto);
            proprietario = service.salvar(proprietario);
            return new ResponseEntity(proprietario, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    @ApiOperation("Atualizar detalhes de um proprietário")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Proprietário atualizado"),
            @ApiResponse(code = 404, message = "Proprietário não encontrado")
    })
    public ResponseEntity put(@PathVariable("id") @ApiParam("Id do proprietário") Long id, @RequestBody ProprietarioDto dto) {
        if (!service.getProprietarioById(id).isPresent()) {
            return new ResponseEntity("Proprietário(a) não encontrado(a)", HttpStatus.NOT_FOUND);
        }
        try {
            Proprietario proprietario = converter(dto);
            proprietario.setId(id);
            service.salvar(proprietario);
            return ResponseEntity.ok(proprietario);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    @ApiOperation("Excluir um proprietário")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Proprietário excluído"),
            @ApiResponse(code = 404, message = "Proprietário não encontrado")
    })
    public ResponseEntity excluir(@PathVariable("id") @ApiParam("Id do proprietário") Long id) {

        Optional<Proprietario> proprietario = service.getProprietarioById(id);

        if (!proprietario.isPresent()) {
            return new ResponseEntity("Proprietário não encontrado", HttpStatus.NOT_FOUND);
        }

        try {
            service.excluir(proprietario.get());
            return new ResponseEntity(HttpStatus.NO_CONTENT);

        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public Proprietario converter(ProprietarioDto dto) {
        ModelMapper modelMapper = new ModelMapper();
        Proprietario proprietario = modelMapper.map(dto, Proprietario.class);
        return proprietario;
    }
}
