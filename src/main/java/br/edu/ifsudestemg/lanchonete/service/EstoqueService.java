package br.edu.ifsudestemg.lanchonete.service;

import br.edu.ifsudestemg.lanchonete.exception.RegraNegocioException;
import br.edu.ifsudestemg.lanchonete.model.entity.Estoque;
import br.edu.ifsudestemg.lanchonete.model.repository.EstoqueRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class EstoqueService {

    private EstoqueRepository repository;

    public EstoqueService(EstoqueRepository repository) {
        this.repository = repository;
    }

    public List<Estoque> getEstoques() {
        return repository.findAll();
    }

    public Optional<Estoque> getEstoqueById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public Estoque salvar(Estoque estoque) {
        validar(estoque);
        return repository.save(estoque);
    }

    @Transactional
    public void excluir(Estoque estoque) {
        Objects.requireNonNull(estoque.getId());
        repository.delete(estoque);
    }

    public void validar(Estoque estoque) {
        if (estoque.getQuantidade() == null || estoque.getQuantidade() == (0)) {
            throw new RegraNegocioException("Quantidade inválida");
        }
    }
}