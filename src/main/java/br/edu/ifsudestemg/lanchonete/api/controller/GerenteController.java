package br.edu.ifsudestemg.lanchonete.api.controller;

import br.edu.ifsudestemg.lanchonete.api.dto.GerenteDto;
import br.edu.ifsudestemg.lanchonete.exception.RegraNegocioException;
import br.edu.ifsudestemg.lanchonete.model.entity.Gerente;
import br.edu.ifsudestemg.lanchonete.service.GerenteService;
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
@RequestMapping("/api/v1/gerentes")
@RequiredArgsConstructor
@Api("API de Gerentes")
@CrossOrigin
public class GerenteController {

    private final GerenteService service;

    @GetMapping()
    @ApiOperation("Obter detalhes de todos os gerentes")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Gerentes encontrados"),
            @ApiResponse(code = 404, message = "Gerentes não encontrados")
    })
    public ResponseEntity get() {
        List<Gerente> gerentes = service.getGerentes();
        return ResponseEntity.ok(gerentes.stream().map(GerenteDto::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @ApiOperation("Obter detalhes de um gerente")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Gerente encontrado"),
            @ApiResponse(code = 404, message = "Gerente não encontrado")
    })
    public ResponseEntity get(@PathVariable("id") @ApiParam("Id do gerente") Long id) {
        Optional<Gerente> gerente = service.getGerenteById(id);
        if (!gerente.isPresent()) {
            return new ResponseEntity("Gerente não encontrado(a)", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(gerente.map(GerenteDto::create));
    }

    @PostMapping()
    @ApiOperation("Inserir novo gerente")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Gerente inserido"),
            @ApiResponse(code = 404, message = "Gerente não inserido")
    })
    public ResponseEntity post(@RequestBody GerenteDto dto) {
        try {
            Gerente gerente = converter(dto);
            gerente = service.salvar(gerente);
            return new ResponseEntity(gerente, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    @ApiOperation("Atualizar detalhes de um gerente")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Gerente atualizado"),
            @ApiResponse(code = 404, message = "Gerente não encontrado")
    })
    public ResponseEntity put(@PathVariable("id") @ApiParam("Id do gerente") Long id, @RequestBody GerenteDto dto) {
        if (!service.getGerenteById(id).isPresent()) {
            return new ResponseEntity("Gerente não encontrado(a)", HttpStatus.NOT_FOUND);
        }
        try {
            Gerente gerente = converter(dto);
            gerente.setId(id);
            service.salvar(gerente);
            return ResponseEntity.ok(gerente);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    @ApiOperation("Excluir um gerente")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Gerente excluído"),
            @ApiResponse(code = 404, message = "Gerente não encontrado")
    })
    public ResponseEntity excluir(@PathVariable("id") @ApiParam("Id do gerente") Long id) {

        Optional<Gerente> gerente = service.getGerenteById(id);

        if (!gerente.isPresent()) {
            return new ResponseEntity("Gerente não encontrado(a)", HttpStatus.NOT_FOUND);
        }

        try {
            service.excluir(gerente.get());
            return new ResponseEntity(HttpStatus.NO_CONTENT);

        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public Gerente converter(GerenteDto dto) {
        ModelMapper modelMapper = new ModelMapper();
        Gerente gerente = modelMapper.map(dto, Gerente.class);
        return gerente;
    }
}
