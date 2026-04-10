package br.edu.ifsudestemg.lanchonete.service;

import br.edu.ifsudestemg.lanchonete.model.entity.Pedido;
import br.edu.ifsudestemg.lanchonete.model.repository.PedidoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PedidoService {

    private PedidoRepository repository;

    public PedidoService(PedidoRepository repository) {
        this.repository = repository;
    }

    public List<Pedido> getPedidos() {
        return repository.findAll();
    }

    public Optional<Pedido> getPedidoById(Long id) {
        return repository.findById(id);
    }
}