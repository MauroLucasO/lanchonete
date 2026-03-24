package br.edu.ifsudestemg.lanchonete.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass

public abstract class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private String nome;

    private String cpf;

    private String telefone;

    private String cep;

    private String cidade;

    private String estado;

    private String logradouro;

    private String email;

    private String senha;
}