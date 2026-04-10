package br.edu.ifsudestemg.lanchonete.model.repository;

import br.edu.ifsudestemg.lanchonete.model.entity.Gerente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GerenteRepository extends JpaRepository<Gerente, Long> {
}