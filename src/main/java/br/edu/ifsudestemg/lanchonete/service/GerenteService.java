package br.edu.ifsudestemg.lanchonete.service;

import br.edu.ifsudestemg.lanchonete.exception.RegraNegocioException;
import br.edu.ifsudestemg.lanchonete.model.entity.Gerente;
import br.edu.ifsudestemg.lanchonete.model.repository.GerenteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class GerenteService {

    private GerenteRepository repository;

    public GerenteService(GerenteRepository repository) {
        this.repository = repository;
    }

    public List<Gerente> getGerentes() {
        return repository.findAll();
    }

    public Optional<Gerente> getGerenteById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public Gerente salvar(Gerente gerente) {
        validar(gerente);
        return repository.save(gerente);
    }

    @Transactional
    public void excluir(Gerente gerente) {
        Objects.requireNonNull(gerente.getId());
        repository.delete(gerente);
    }

    public void validar(Gerente gerente) {
        if (gerente.getNome() == null || gerente.getNome().trim().equals("")) {
            throw new RegraNegocioException("Nome inválido");
        }
        if (gerente.getCpf() == null || gerente.getCpf().trim().equals("")) {
            throw new RegraNegocioException("CPF inválido");
        }
        if (gerente.getEmail() == null || gerente.getEmail().trim().equals("")) {
            throw new RegraNegocioException("Email inválido");
        }
    }
}