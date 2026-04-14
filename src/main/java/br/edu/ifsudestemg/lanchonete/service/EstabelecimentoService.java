package br.edu.ifsudestemg.lanchonete.service;

import br.edu.ifsudestemg.lanchonete.exception.RegraNegocioException;
import br.edu.ifsudestemg.lanchonete.model.entity.Estabelecimento;
import br.edu.ifsudestemg.lanchonete.model.repository.EstabelecimentoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
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

    @Transactional
    public Estabelecimento salvar(Estabelecimento estabelecimento) {
        validar(estabelecimento);
        return repository.save(estabelecimento);
    }

    @Transactional
    public void excluir(Estabelecimento estabelecimento) {
        Objects.requireNonNull(estabelecimento.getId());
        repository.delete(estabelecimento);
    }

    public void validar(Estabelecimento estabelecimento) {
        if (estabelecimento.getNome() == null || estabelecimento.getNome().trim().equals("")) {
            throw new RegraNegocioException("Nome inválido");
        }
        if (estabelecimento.getCnpj() == null || estabelecimento.getCnpj().trim().equals("")) {
            throw new RegraNegocioException("CNPJ inválido");
        }
    }
}