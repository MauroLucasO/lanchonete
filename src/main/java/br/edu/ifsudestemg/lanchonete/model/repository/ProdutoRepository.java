package br.edu.ifsudestemg.lanchonete.model.repository;

import br.edu.ifsudestemg.lanchonete.model.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
}