package br.edu.ifsudestemg.lanchonete.api.controller;

import br.edu.ifsudestemg.lanchonete.api.dto.CategoriaDto;
import br.edu.ifsudestemg.lanchonete.api.dto.ProprietarioDto;
import br.edu.ifsudestemg.lanchonete.exception.RegraNegocioException;
import br.edu.ifsudestemg.lanchonete.model.entity.Categoria;
import br.edu.ifsudestemg.lanchonete.model.entity.Proprietario;
import br.edu.ifsudestemg.lanchonete.service.ProprietarioService;
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
@CrossOrigin
public class ProprietarioController {

    private final ProprietarioService service;

    @GetMapping()
    public ResponseEntity get() {
        List<Proprietario> proprietarios = service.getProprietarios();
        return ResponseEntity.ok(proprietarios.stream().map(ProprietarioDto::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<Proprietario> proprietario = service.getProprietarioById(id);
        if (!proprietario.isPresent()) {
            return new ResponseEntity("Proprietário não encontrado", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(proprietario.map(ProprietarioDto::create));
    }

    @PostMapping()
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
    public ResponseEntity put(@PathVariable("id") Long id, @RequestBody ProprietarioDto dto) {
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
    public ResponseEntity excluir(@PathVariable("id") Long id) {

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
