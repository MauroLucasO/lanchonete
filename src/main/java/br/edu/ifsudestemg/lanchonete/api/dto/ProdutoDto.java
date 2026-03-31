package br.edu.ifsudestemg.lanchonete.api.dto;

import br.edu.ifsudestemg.lanchonete.model.entity.Produto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class ProdutoDto {

    private Long id;
    private String nome;
    private Float valor;
    private String descricao;
    private Long idCategoria;
    private String nomeCategoria;
    private Long idEstabelecimento;
    private Long idItemPedido;
    private Long idEstoque;
    private Integer quantidade;

    public static ProdutoDto create(Produto produto) {
        ModelMapper modelMapper = new ModelMapper();
        ProdutoDto dto = modelMapper.map(produto, ProdutoDto.class);
        dto.quantidade = produto.getEstoque().getQuantidade();
        dto.nomeCategoria = produto.getCategoria().getNome();
        return dto;
    }
}