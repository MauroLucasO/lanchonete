package br.edu.ifsudestemg.lanchonete.service;

import br.edu.ifsudestemg.lanchonete.exception.RegraNegocioException;
import br.edu.ifsudestemg.lanchonete.model.entity.Pedido;
import br.edu.ifsudestemg.lanchonete.model.repository.PedidoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
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
    @Transactional
    public Pedido salvar(Pedido pedido) {
        validar(pedido);
        return repository.save(pedido);
    }
    @Transactional
    public void excluir(Pedido pedido) {
        Objects.requireNonNull(pedido.getId());
        repository.delete(pedido);
    }
    public void validar(Pedido pedido) {
        if (pedido.getItemPedido() == null) {
            throw new RegraNegocioException("Pedido inválido");
        }
        if (pedido.getValorTotal() == null || pedido.getValorTotal() == 0) {
            throw new RegraNegocioException("Valor Total inválido");
        }
        if (pedido.getEstabelecimento() == null) {
            throw new RegraNegocioException("Estabelecimento inválido");
        }
        if (pedido.getCliente() == null) {
            throw new RegraNegocioException("Cliente inválido");
        }
    }
}