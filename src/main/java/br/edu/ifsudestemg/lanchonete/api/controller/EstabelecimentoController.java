package br.edu.ifsudestemg.lanchonete.api.controller;

import br.edu.ifsudestemg.lanchonete.api.dto.EstabelecimentoDto;
import br.edu.ifsudestemg.lanchonete.exception.RegraNegocioException;
import br.edu.ifsudestemg.lanchonete.model.entity.Estabelecimento;
import br.edu.ifsudestemg.lanchonete.service.EstabelecimentoService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/estabelecimentos")
@RequiredArgsConstructor
@CrossOrigin
public class EstabelecimentoController {

    private final EstabelecimentoService service;

    @GetMapping()
    public ResponseEntity get() {
        List<Estabelecimento> estabelecimentos = service.getEstabelecimentos();
        return ResponseEntity.ok(estabelecimentos.stream().map(EstabelecimentoDto::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<Estabelecimento> estabelecimento = service.getEstabelecimentoById(id);
        if (!estabelecimento.isPresent()) {
            return new ResponseEntity("Estabelecimento não encontrado", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(estabelecimento.map(EstabelecimentoDto::create));
    }

    @PostMapping()
    public ResponseEntity post(@RequestBody EstabelecimentoDto dto) {
        try {
            Estabelecimento estabelecimento = converter(dto);
            estabelecimento = service.salvar(estabelecimento);
            return new ResponseEntity(estabelecimento, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    public ResponseEntity put(@PathVariable("id") Long id, @RequestBody EstabelecimentoDto dto) {
        if (!service.getEstabelecimentoById(id).isPresent()) {
            return new ResponseEntity("Estabelecimento não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            Estabelecimento estabelecimento = converter(dto);
            estabelecimento.setId(id);
            service.salvar(estabelecimento);
            return ResponseEntity.ok(estabelecimento);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity excluir(@PathVariable("id") Long id) {

        Optional<Estabelecimento> estabelecimento = service.getEstabelecimentoById(id);

        if (!estabelecimento.isPresent()) {
            return new ResponseEntity("Estabelecimento não encontrado", HttpStatus.NOT_FOUND);
        }

        try {
            service.excluir(estabelecimento.get());
            return new ResponseEntity(HttpStatus.NO_CONTENT);

        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public Estabelecimento converter(EstabelecimentoDto dto) {
        ModelMapper modelMapper = new ModelMapper();
        Estabelecimento estabelecimento = modelMapper.map(dto, Estabelecimento.class);
        return estabelecimento;
    }
}
