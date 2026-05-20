package br.edu.ifsudestemg.lanchonete.api.controller;

import br.edu.ifsudestemg.lanchonete.api.dto.CategoriaDto;
import br.edu.ifsudestemg.lanchonete.api.dto.EstoqueDto;
import br.edu.ifsudestemg.lanchonete.exception.RegraNegocioException;
import br.edu.ifsudestemg.lanchonete.model.entity.Categoria;
import br.edu.ifsudestemg.lanchonete.model.entity.Estoque;
import br.edu.ifsudestemg.lanchonete.service.EstoqueService;
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
@RequestMapping("/api/v1/estoques")
@RequiredArgsConstructor
@Api("API de Estoques")
@CrossOrigin
public class EstoqueController {

    private final EstoqueService service;

    @GetMapping()
    @ApiOperation("Obter detalhes de todos os estoques")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Estoques encontrados"),
            @ApiResponse(code = 404, message = "Estoques não encontrados")
    })
    public ResponseEntity get() {
        List<Estoque> estoques = service.getEstoques();
        return ResponseEntity.ok(estoques.stream().map(EstoqueDto::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @ApiOperation("Obter detalhes de um estoque")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Estoque encontrado"),
            @ApiResponse(code = 404, message = "Estoque não encontrado")
    })
    public ResponseEntity get(@PathVariable("id") @ApiParam("Id do estoque") Long id) {
        Optional<Estoque> estoque = service.getEstoqueById(id);
        if (!estoque.isPresent()) {
            return new ResponseEntity("Estoque não encontrado", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(estoque.map(EstoqueDto::create));
    }

    @PostMapping()
    @ApiOperation("Inserir novo estoque")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Estoque inserido"),
            @ApiResponse(code = 404, message = "Estoque não inserido")
    })
    public ResponseEntity post(@RequestBody EstoqueDto dto) {
        try {
            Estoque estoque = converter(dto);
            estoque = service.salvar(estoque);
            return new ResponseEntity(estoque, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    @ApiOperation("Atualizar detalhes de um estoque")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Estoque atualizado"),
            @ApiResponse(code = 404, message = "Estoque não encontrado")
    })
    public ResponseEntity put(@PathVariable("id") @ApiParam("Id do estoque") Long id, @RequestBody EstoqueDto dto) {
        if (!service.getEstoqueById(id).isPresent()) {
            return new ResponseEntity("Estoque não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            Estoque estoque = converter(dto);
            estoque.setId(id);
            service.salvar(estoque);
            return ResponseEntity.ok(estoque);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    @ApiOperation("Excluir um estoque")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Estoque excluído"),
            @ApiResponse(code = 404, message = "Estoque não encontrado")
    })
    public ResponseEntity excluir(@PathVariable("id") @ApiParam("Id do estoque") Long id) {

        Optional<Estoque> estoque = service.getEstoqueById(id);

        if (!estoque.isPresent()) {
            return new ResponseEntity("Estoque não encontrado", HttpStatus.NOT_FOUND);
        }

        try {
            service.excluir(estoque.get());
            return new ResponseEntity(HttpStatus.NO_CONTENT);

        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public Estoque converter(EstoqueDto dto) {
        ModelMapper modelMapper = new ModelMapper();
        Estoque estoque = modelMapper.map(dto, Estoque.class);
        return estoque;
    }
}
