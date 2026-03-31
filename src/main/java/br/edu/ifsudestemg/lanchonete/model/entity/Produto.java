package br.edu.ifsudestemg.lanchonete.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    private String nome;

    private Float valor;

    private String descricao;

    private Long idCategoria;

    @ManyToOne
    private Categoria categoria;

    @ManyToOne
    private Estabelecimento estabelecimento;

    @ManyToOne
    private ItemPedido itemPedido;

    @ManyToOne
    private Estoque estoque;

}