package br.edu.ifsudestemg.lanchonete.api.dto;

import br.edu.ifsudestemg.lanchonete.model.entity.Estoque;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class EstoqueDto {

    private Integer quantidade;
    private Long idProduto;
    private String nomeProduto;

    public static EstoqueDto create(Estoque estoque) {
        ModelMapper modelMapper = new ModelMapper();
        EstoqueDto dto = modelMapper.map(estoque, EstoqueDto.class);

        if (estoque.getProduto() != null) {
            dto.nomeProduto = estoque.getProduto().getNome();
        }

        return dto;
    }
}