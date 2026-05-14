package br.edu.ifsudestemg.lanchonete.api.controller;

import br.edu.ifsudestemg.lanchonete.api.dto.ProdutoDto;
import br.edu.ifsudestemg.lanchonete.exception.RegraNegocioException;
import br.edu.ifsudestemg.lanchonete.model.entity.Produto;
import br.edu.ifsudestemg.lanchonete.service.ProdutoService;
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
@CrossOrigin
public class ProdutoController {

    private final ProdutoService service;

    @GetMapping()
    public ResponseEntity get() {
        List<Produto> produtos = service.getProdutos();
        return ResponseEntity.ok(produtos.stream().map(ProdutoDto::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<Produto> produto = service.getProdutoById(id);
        if (!produto.isPresent()) {
            return new ResponseEntity("Produto não encontrado", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(produto.map(ProdutoDto::create));
    }

    @PostMapping()
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
    public ResponseEntity put(@PathVariable("id") Long id, @RequestBody ProdutoDto dto) {
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

    public Produto converter(ProdutoDto dto) {
        ModelMapper modelMapper = new ModelMapper();
        Produto produto = modelMapper.map(dto, Produto.class);
        return produto;
    }
}