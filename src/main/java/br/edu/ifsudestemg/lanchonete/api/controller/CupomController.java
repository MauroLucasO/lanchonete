package br.edu.ifsudestemg.lanchonete.api.controller;

import br.edu.ifsudestemg.lanchonete.api.dto.CupomDto;
import br.edu.ifsudestemg.lanchonete.exception.RegraNegocioException;
import br.edu.ifsudestemg.lanchonete.model.entity.Cupom;
import br.edu.ifsudestemg.lanchonete.service.CupomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/cupons")
@RequiredArgsConstructor
@CrossOrigin
public class CupomController {

    private final CupomService service;

    @GetMapping()
    public ResponseEntity get() {
        List<Cupom> cupons = service.getCupons();
        return ResponseEntity.ok(cupons.stream().map(CupomDto::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<Cupom> cupom = service.getCupomById(id);
        if (!cupom.isPresent()) {
            return new ResponseEntity("Cupom não encontrado", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(cupom.map(CupomDto::create));
    }
}
