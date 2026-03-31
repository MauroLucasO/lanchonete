package br.edu.ifsudestemg.lanchonete.api.dto;

import br.edu.ifsudestemg.lanchonete.model.entity.ItemPedido;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class ItemPedidoDto {

    private Long id;
    private Integer quantidade;
    private Float valorParcial;
    private Long idProduto;

    public static ItemPedidoDto create(ItemPedido itemPedido) {
        ModelMapper modelMapper = new ModelMapper();
        ItemPedidoDto dto = modelMapper.map(itemPedido, ItemPedidoDto.class);
        return dto;
    }
}