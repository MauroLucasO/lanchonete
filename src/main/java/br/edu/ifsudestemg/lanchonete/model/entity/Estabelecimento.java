package br.edu.ifsudestemg.lanchonete.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Estabelecimento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    private String nome;

    private String cnpj;

    private String telefone;

    private String cidade;

    private String logradouro;

    private String pontoDeReferencia;

    @ManyToOne
    private Proprietario proprietario;
}