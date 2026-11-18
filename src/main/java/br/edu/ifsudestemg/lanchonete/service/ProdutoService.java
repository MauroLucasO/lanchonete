package br.edu.ifsudestemg.lanchonete.service;

import br.edu.ifsudestemg.lanchonete.exception.RegraNegocioException;
import br.edu.ifsudestemg.lanchonete.model.entity.Produto;
import br.edu.ifsudestemg.lanchonete.model.repository.ProdutoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ProdutoService {

    private ProdutoRepository repository;

    public ProdutoService(ProdutoRepository repository) {
        this.repository = repository;
    }

    public List<Produto> getProdutos() {
        return repository.findAll();
    }

    public Optional<Produto> getProdutoById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public Produto salvar(Produto produto) {
        validar(produto);
        return repository.save(produto);
    }

    @Transactional
    public void excluir(Produto produto) {
        Objects.requireNonNull(produto.getId());
        repository.delete(produto);
    }

    public void validar(Produto produto) {
        if (produto == null) {
            throw new RegraNegocioException("Produto inválido");
        }
        if (produto.getNome() == null || produto.getNome().trim().equals("") || produto.getNome().trim().length() < 2) {
            throw new RegraNegocioException("Nome inválido");
        }
        if (produto.getValor() == null || produto.getValor().doubleValue() <= 0 || produto.getValor().doubleValue() > 10000) {
            throw new RegraNegocioException("Valor inválido");
        }
        if (produto.getDescricao() == null || produto.getDescricao().trim().equals("") || produto.getDescricao().trim().length() < 5) {
            throw new RegraNegocioException("Descrição inválida");
        }
    }
}