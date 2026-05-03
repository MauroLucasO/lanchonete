package br.edu.ifsudestemg.lanchonete.api.controller;

import br.edu.ifsudestemg.lanchonete.api.dto.PedidoDto;
import br.edu.ifsudestemg.lanchonete.exception.RegraNegocioException;
import br.edu.ifsudestemg.lanchonete.model.entity.Pedido;
import br.edu.ifsudestemg.lanchonete.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/pedidos")
@RequiredArgsConstructor
@CrossOrigin
public class PedidoController {

    private final PedidoService service;

    @GetMapping()
    public ResponseEntity get() {
        List<Pedido> pedidos = service.getPedidos();
        return ResponseEntity.ok(pedidos.stream().map(PedidoDto::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<Pedido> pedido = service.getPedidoById(id);
        if (!pedido.isPresent()) {
            return new ResponseEntity("Pedido não encontrado", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(pedido.map(PedidoDto::create));
    }
}
