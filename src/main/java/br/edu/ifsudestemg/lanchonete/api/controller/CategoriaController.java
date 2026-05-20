package br.edu.ifsudestemg.lanchonete.api.controller;

import br.edu.ifsudestemg.lanchonete.api.dto.CategoriaDto;
import br.edu.ifsudestemg.lanchonete.exception.RegraNegocioException;
import br.edu.ifsudestemg.lanchonete.model.entity.Categoria;
import br.edu.ifsudestemg.lanchonete.service.CategoriaService;
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
@RequestMapping("/api/v1/categorias")
@RequiredArgsConstructor
@Api("API de Categorias")
@CrossOrigin
public class CategoriaController {

    private final CategoriaService service;

    @GetMapping()
    @ApiOperation("Obter detalhes de todas as categorias")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Categorias encontradas"),
            @ApiResponse(code = 404, message = "Categorias não encontradas")
    })
    public ResponseEntity get() {
        List<Categoria> categorias = service.getCategorias();
        return ResponseEntity.ok(categorias.stream().map(CategoriaDto::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @ApiOperation("Obter detalhes de uma categoria")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Categoria encontrada"),
            @ApiResponse(code = 404, message = "Categoria não encontrada")
    })
    public ResponseEntity get(@PathVariable("id") @ApiParam("Id da categoria") Long id) {
        Optional<Categoria> categoria = service.getCategoriaById(id);
        if (!categoria.isPresent()) {
            return new ResponseEntity("Categoria não encontrada", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(categoria.map(CategoriaDto::create));
    }

    @PostMapping()
    @ApiOperation("Inserir nova categoria")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Categoria inserida"),
            @ApiResponse(code = 404, message = "Categoria não inserida")
    })
    public ResponseEntity post(@RequestBody CategoriaDto dto) {
        try {
            Categoria categoria = converter(dto);
            categoria = service.salvar(categoria);
            return new ResponseEntity(categoria, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    @ApiOperation("Atualizar detalhes de uma categoria")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Categoria atualizada"),
            @ApiResponse(code = 404, message = "Categoria não encontrada")
    })
    public ResponseEntity put(@PathVariable("id") @ApiParam("Id da categoria") Long id, @RequestBody CategoriaDto dto) {
        if (!service.getCategoriaById(id).isPresent()) {
            return new ResponseEntity("Categoria não encontrada", HttpStatus.NOT_FOUND);
        }
        try {
            Categoria categoria = converter(dto);
            categoria.setId(id);
            service.salvar(categoria);
            return ResponseEntity.ok(categoria);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    @ApiOperation("Excluir uma categoria")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Categoria excluída"),
            @ApiResponse(code = 404, message = "Categoria não encontrada")
    })
    public ResponseEntity excluir(@PathVariable("id") @ApiParam("Id da categoria") Long id) {

        Optional<Categoria> categoria = service.getCategoriaById(id);

        if (!categoria.isPresent()) {
            return new ResponseEntity("Categoria não encontrada", HttpStatus.NOT_FOUND);
        }

        try {
            service.excluir(categoria.get());
            return new ResponseEntity(HttpStatus.NO_CONTENT);

        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public Categoria converter(CategoriaDto dto) {
        ModelMapper modelMapper = new ModelMapper();
        Categoria categoria = modelMapper.map(dto, Categoria.class);
        return categoria;
    }

}