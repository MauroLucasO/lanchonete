package br.edu.ifsudestemg.lanchonete.service;

import br.edu.ifsudestemg.lanchonete.model.entity.Proprietario;
import br.edu.ifsudestemg.lanchonete.model.repository.ProprietarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
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
}