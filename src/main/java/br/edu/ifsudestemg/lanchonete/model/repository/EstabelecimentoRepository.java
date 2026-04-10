package br.edu.ifsudestemg.lanchonete.model.repository;

import br.edu.ifsudestemg.lanchonete.model.entity.Estabelecimento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EstabelecimentoRepository extends JpaRepository<Estabelecimento, Long> {
}