package br.edu.ifsudestemg.lanchonete.api.dto;

import br.edu.ifsudestemg.lanchonete.model.entity.Cupom;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class CupomDto {

    private Long id;
    private Double desconto;
    private String duracao;
    private String nome;
    private Long idEstabelecimento;

    public static CupomDto create(Cupom cupom) {
        ModelMapper modelMapper = new ModelMapper();
        CupomDto dto = modelMapper.map(cupom, CupomDto.class);
        return dto;
    }
}