package br.edu.ifsudestemg.lanchonete.model.repository;

import br.edu.ifsudestemg.lanchonete.model.entity.Proprietario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProprietarioRepository extends JpaRepository<Proprietario, Long> {
}