package br.edu.ifsudestemg.lanchonete.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String login;

    private String nome;

    private String cpf;

    private String telefone;

    private String cep;

    private String cidade;

    private String estado;

    private String logradouro;

    private String email;

    private String senha;

    private boolean admin;
}