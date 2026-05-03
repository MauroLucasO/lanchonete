package br.edu.ifsudestemg.lanchonete.api.controller;

import br.edu.ifsudestemg.lanchonete.api.dto.GerenteDto;
import br.edu.ifsudestemg.lanchonete.exception.RegraNegocioException;
import br.edu.ifsudestemg.lanchonete.model.entity.Gerente;
import br.edu.ifsudestemg.lanchonete.service.GerenteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/gerentes")
@RequiredArgsConstructor
@CrossOrigin
public class GerenteController {

    private final GerenteService service;

    @GetMapping()
    public ResponseEntity get() {
        List<Gerente> gerentes = service.getGerentes();
        return ResponseEntity.ok(gerentes.stream().map(GerenteDto::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<Gerente> gerente = service.getGerenteById(id);
        if (!gerente.isPresent()) {
            return new ResponseEntity("Gerente não encontrado(a)", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(gerente.map(GerenteDto::create));
    }
}
