package br.edu.ifsudestemg.lanchonete.model.repository;

import br.edu.ifsudestemg.lanchonete.model.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}