package br.edu.ifsudestemg.lanchonete.api.controller;

import br.edu.ifsudestemg.lanchonete.api.dto.EstabelecimentoDto;
import br.edu.ifsudestemg.lanchonete.exception.RegraNegocioException;
import br.edu.ifsudestemg.lanchonete.model.entity.Estabelecimento;
import br.edu.ifsudestemg.lanchonete.service.EstabelecimentoService;
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
@RequestMapping("/api/v1/estabelecimentos")
@RequiredArgsConstructor
@Api("API de Estabelecimentos")
@CrossOrigin
public class EstabelecimentoController {

    private final EstabelecimentoService service;

    @GetMapping()
    @ApiOperation("Obter detalhes de todos os estabelecimentos")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Estabelecimentos encontrados"),
            @ApiResponse(code = 404, message = "Estabelecimentos não encontrados")
    })
    public ResponseEntity get() {
        List<Estabelecimento> estabelecimentos = service.getEstabelecimentos();
        return ResponseEntity.ok(estabelecimentos.stream().map(EstabelecimentoDto::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @ApiOperation("Obter detalhes de um estabelecimento")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Estabelecimento encontrado"),
            @ApiResponse(code = 404, message = "Estabelecimento não encontrado")
    })
    public ResponseEntity get(@PathVariable("id") @ApiParam("Id do estabelecimento") Long id) {
        Optional<Estabelecimento> estabelecimento = service.getEstabelecimentoById(id);
        if (!estabelecimento.isPresent()) {
            return new ResponseEntity("Estabelecimento não encontrado", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(estabelecimento.map(EstabelecimentoDto::create));
    }

    @PostMapping()
    @ApiOperation("Inserir novo estabelecimento")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Estabelecimento inserido"),
            @ApiResponse(code = 404, message = "Estabelecimento não inserido")
    })
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
    @ApiOperation("Atualizar detalhes de um estabelecimento")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Estabelecimento atualizado"),
            @ApiResponse(code = 404, message = "Estabelecimento não encontrado")
    })
    public ResponseEntity put(@PathVariable("id") @ApiParam("Id do estabelecimento") Long id, @RequestBody EstabelecimentoDto dto) {
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
    @ApiOperation("Excluir um estabelecimento")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Estabelecimento excluído"),
            @ApiResponse(code = 404, message = "Estabelecimento não encontrado")
    })
    public ResponseEntity excluir(@PathVariable("id") @ApiParam("Id do estabelecimento") Long id) {

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
