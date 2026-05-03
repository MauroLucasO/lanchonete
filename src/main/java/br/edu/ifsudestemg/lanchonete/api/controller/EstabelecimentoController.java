package br.edu.ifsudestemg.lanchonete.api.controller;

import br.edu.ifsudestemg.lanchonete.api.dto.EstabelecimentoDto;
import br.edu.ifsudestemg.lanchonete.exception.RegraNegocioException;
import br.edu.ifsudestemg.lanchonete.model.entity.Estabelecimento;
import br.edu.ifsudestemg.lanchonete.service.EstabelecimentoService;
import lombok.RequiredArgsConstructor;
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
}
