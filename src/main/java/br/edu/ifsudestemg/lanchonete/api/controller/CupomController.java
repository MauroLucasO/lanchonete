package br.edu.ifsudestemg.lanchonete.api.controller;

import br.edu.ifsudestemg.lanchonete.api.dto.CupomDto;
import br.edu.ifsudestemg.lanchonete.exception.RegraNegocioException;
import br.edu.ifsudestemg.lanchonete.model.entity.Cupom;
import br.edu.ifsudestemg.lanchonete.service.CupomService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
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

    @PostMapping()
    public ResponseEntity post(@RequestBody CupomDto dto) {
        try {
            Cupom cupom = converter(dto);
            cupom = service.salvar(cupom);
            return new ResponseEntity(cupom, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    public ResponseEntity put(@PathVariable("id") Long id, @RequestBody CupomDto dto) {
        if (!service.getCupomById(id).isPresent()) {
            return new ResponseEntity("Cupom não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            Cupom cupom = converter(dto);
            cupom.setId(id);
            service.salvar(cupom);
            return ResponseEntity.ok(cupom);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public Cupom converter(CupomDto dto) {
        ModelMapper modelMapper = new ModelMapper();
        Cupom cupom = modelMapper.map(dto, Cupom.class);
        return cupom;
    }
}
