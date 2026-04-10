package br.edu.ifsudestemg.lanchonete.service;

import br.edu.ifsudestemg.lanchonete.model.entity.ItemPedido;
import br.edu.ifsudestemg.lanchonete.model.repository.ItemPedidoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ItemPedidoService {

    private ItemPedidoRepository repository;

    public ItemPedidoService(ItemPedidoRepository repository) {
        this.repository = repository;
    }

    public List<ItemPedido> getItensPedido() {
        return repository.findAll();
    }

    public Optional<ItemPedido> getItemPedidoById(Long id) {
        return repository.findById(id);
    }
}