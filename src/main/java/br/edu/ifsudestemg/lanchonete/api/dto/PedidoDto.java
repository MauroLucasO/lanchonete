package br.edu.ifsudestemg.lanchonete.api.dto;

import br.edu.ifsudestemg.lanchonete.model.entity.Pedido;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class PedidoDto {

    private Long id;
    private Float valorTotal;
    private Long idEstabelecimento;
    private Long idItemPedido;
    private Float valorParcial;
    private Long idCliente;
    private String nomeCliente;
    private String telefone;
    private String logradouro;

    public static PedidoDto create(Pedido pedido) {
        ModelMapper modelMapper = new ModelMapper();
        PedidoDto dto = modelMapper.map(pedido, PedidoDto.class);
        dto.nomeCliente = pedido.getCliente().getNome();
        dto.logradouro = pedido.getCliente().getLogradouro();
        dto.telefone = pedido.getCliente().getTelefone();
        dto.valorParcial = pedido.getItemPedido().getValorParcial();
        return dto;
    }
}