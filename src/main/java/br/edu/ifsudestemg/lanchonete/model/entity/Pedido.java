package br.edu.ifsudestemg.lanchonete.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    private Float valorTotal;

    @ManyToOne
    private Cliente cliente;

    @ManyToOne
    private Estabelecimento estabelecimento;

    @ManyToOne
    private ItemPedido ItemPedido;
}