package br.edu.ifsudestemg.lanchonete.api.controller;

import br.edu.ifsudestemg.lanchonete.api.dto.ProprietarioDto;
import br.edu.ifsudestemg.lanchonete.model.entity.Proprietario;
import br.edu.ifsudestemg.lanchonete.service.ProprietarioService;
import lombok.RequiredArgsConstructor;
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
}
