package br.edu.ifsudestemg.lanchonete.service;

import br.edu.ifsudestemg.lanchonete.exception.RegraNegocioException;
import br.edu.ifsudestemg.lanchonete.model.entity.Proprietario;
import br.edu.ifsudestemg.lanchonete.model.repository.ProprietarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ProprietarioService {

    private ProprietarioRepository repository;

    public ProprietarioService(ProprietarioRepository repository) {
        this.repository = repository;
    }

    public List<Proprietario> getProprietarios() {
        return repository.findAll();
    }

    public Optional<Proprietario> getProprietarioById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public Proprietario salvar(Proprietario proprietario) {
        validar(proprietario);
        return repository.save(proprietario);
    }

    @Transactional
    public void excluir(Proprietario proprietario) {
        Objects.requireNonNull(proprietario.getId());
        repository.delete(proprietario);
    }

    public void validar(Proprietario proprietario) {
        if (proprietario.getNome() == null || proprietario.getNome().trim().equals("")) {
            throw new RegraNegocioException("Nome inválido");
        }
        if (proprietario.getCpf() == null || proprietario.getCpf().trim().equals("")) {
            throw new RegraNegocioException("CPF inválido");
        }
        if (proprietario.getEmail() == null || proprietario.getEmail().trim().equals("")) {
            throw new RegraNegocioException("Email inválido");
        }
    }
}