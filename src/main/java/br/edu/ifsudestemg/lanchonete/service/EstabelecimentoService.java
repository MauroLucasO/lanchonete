package br.edu.ifsudestemg.lanchonete.service;

import br.edu.ifsudestemg.lanchonete.model.entity.Estabelecimento;
import br.edu.ifsudestemg.lanchonete.model.repository.EstabelecimentoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EstabelecimentoService {

    private EstabelecimentoRepository repository;

    public EstabelecimentoService(EstabelecimentoRepository repository) {
        this.repository = repository;
    }

    public List<Estabelecimento> getEstabelecimentos() {
        return repository.findAll();
    }

    public Optional<Estabelecimento> getEstabelecimentoById(Long id) {
        return repository.findById(id);
    }
}