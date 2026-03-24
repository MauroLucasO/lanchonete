package br.edu.ifsudestemg.lanchonete.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Gerente extends Usuario {

    @ManyToOne
    private Estabelecimento estabelecimento;
}