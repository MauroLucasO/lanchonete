package br.edu.ifsudestemg.lanchonete.service;

import br.edu.ifsudestemg.lanchonete.model.entity.Estoque;
import br.edu.ifsudestemg.lanchonete.model.repository.EstoqueRepository;
import org.springframework.stereotype.Service;

import java.util.List;
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
}