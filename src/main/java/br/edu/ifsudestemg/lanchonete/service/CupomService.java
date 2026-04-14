package br.edu.ifsudestemg.lanchonete.service;

import br.edu.ifsudestemg.lanchonete.exception.RegraNegocioException;
import br.edu.ifsudestemg.lanchonete.model.entity.Cupom;
import br.edu.ifsudestemg.lanchonete.model.repository.CupomRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
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

    @Transactional
    public Cupom salvar(Cupom cupom) {
        validar(cupom);
        return repository.save(cupom);
    }

    @Transactional
    public void excluir(Cupom cupom) {
        Objects.requireNonNull(cupom.getId());
        repository.delete(cupom);
    }

    public void validar(Cupom cupom) {
        if (cupom.getNome() == null || cupom.getNome().trim().equals("")) {
            throw new RegraNegocioException("Nome inválido");
        }
        if (cupom.getDuracao() == null || cupom.getDuracao() == null) {
            throw new RegraNegocioException("Duração inválida");
        }
        if (cupom.getDesconto() == null || cupom.getDesconto() == 0) {
            throw new RegraNegocioException("Desconto inválido");
        }
    }
}