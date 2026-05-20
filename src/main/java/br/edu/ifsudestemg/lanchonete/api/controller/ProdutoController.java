package br.edu.ifsudestemg.lanchonete.api.controller;

import br.edu.ifsudestemg.lanchonete.api.dto.ProdutoDto;
import br.edu.ifsudestemg.lanchonete.exception.RegraNegocioException;
import br.edu.ifsudestemg.lanchonete.model.entity.Produto;
import br.edu.ifsudestemg.lanchonete.service.ProdutoService;
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
@RequestMapping("/api/v1/produtos")
@RequiredArgsConstructor
@Api("API de Produtos")
@CrossOrigin
public class ProdutoController {

    private final ProdutoService service;

    @GetMapping()
    @ApiOperation("Obter detalhes de todos os produtos")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Produtos encontrados"),
            @ApiResponse(code = 404, message = "Produtos não encontrados")
    })
    public ResponseEntity get() {
        List<Produto> produtos = service.getProdutos();
        return ResponseEntity.ok(produtos.stream().map(ProdutoDto::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @ApiOperation("Obter detalhes de um produto")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Produto encontrado"),
            @ApiResponse(code = 404, message = "Produto não encontrado")
    })
    public ResponseEntity get(@PathVariable("id") @ApiParam("Id do produto") Long id) {
        Optional<Produto> produto = service.getProdutoById(id);
        if (!produto.isPresent()) {
            return new ResponseEntity("Produto não encontrado", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(produto.map(ProdutoDto::create));
    }

    @PostMapping()
    @ApiOperation("Inserir novo produto")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Produto inserido"),
            @ApiResponse(code = 404, message = "Produto não inserido")
    })
    public ResponseEntity post(@RequestBody ProdutoDto dto) {
        try {
            Produto produto = converter(dto);
            produto = service.salvar(produto);
            return new ResponseEntity(produto, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    @ApiOperation("Atualizar detalhes de um produto")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Produto atualizado"),
            @ApiResponse(code = 404, message = "Produto não encontrado")
    })
    public ResponseEntity put(@PathVariable("id") @ApiParam("Id do produto") Long id, @RequestBody ProdutoDto dto) {
        if (!service.getProdutoById(id).isPresent()) {
            return new ResponseEntity("Produto não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            Produto produto = converter(dto);
            produto.setId(id);
            service.salvar(produto);
            return ResponseEntity.ok(produto);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    @ApiOperation("Excluir um produto")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Produto excluído"),
            @ApiResponse(code = 404, message = "Produto não excluído")
    })
    public ResponseEntity excluir(@PathVariable("id") @ApiParam("Id do produto") Long id) {

        Optional<Produto> produto = service.getProdutoById(id);

        if (!produto.isPresent()) {
            return new ResponseEntity("Produto não encontrado", HttpStatus.NOT_FOUND);
        }

        try {
            service.excluir(produto.get());
            return new ResponseEntity(HttpStatus.NO_CONTENT);

        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public Produto converter(ProdutoDto dto) {
        ModelMapper modelMapper = new ModelMapper();
        Produto produto = modelMapper.map(dto, Produto.class);
        return produto;
    }
}