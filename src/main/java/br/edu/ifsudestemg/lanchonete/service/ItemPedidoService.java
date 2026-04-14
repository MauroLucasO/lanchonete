package br.edu.ifsudestemg.lanchonete.service;

import br.edu.ifsudestemg.lanchonete.exception.RegraNegocioException;
import br.edu.ifsudestemg.lanchonete.model.entity.ItemPedido;
import br.edu.ifsudestemg.lanchonete.model.repository.ItemPedidoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
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
    @Transactional
    public ItemPedido salvar(ItemPedido itemPedido) {
        validar(itemPedido);
        return repository.save(itemPedido);
    }

    @Transactional
    public void excluir(ItemPedido itemPedido) {
        Objects.requireNonNull(itemPedido.getId());
        repository.delete(itemPedido);
    }
    public void validar(ItemPedido itemPedido) {
        if (itemPedido.getQuantidade() == null || itemPedido.getQuantidade() == 0) {
            throw new RegraNegocioException("Matrícula inválida");
        }
        if (itemPedido.getValorParcial() == null || itemPedido.getValorParcial() == 0) {
            throw new RegraNegocioException("Valor Total inválido");
        }
        if (itemPedido.getProduto() == null) {
            throw new RegraNegocioException("Produto inválido");
        }
    }
}