package br.edu.ifsudestemg.lanchonete.service;

import br.edu.ifsudestemg.lanchonete.model.entity.Cupom;
import br.edu.ifsudestemg.lanchonete.model.repository.CupomRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CupomService {

    private CupomRepository repository;

    public CupomService(CupomRepository repository) {
        this.repository = repository;
    }

    public List<Cupom> getCupons() {
        return repository.findAll();
    }

    public Optional<Cupom> getCupomById(Long id) {
        return repository.findById(id);
    }
}