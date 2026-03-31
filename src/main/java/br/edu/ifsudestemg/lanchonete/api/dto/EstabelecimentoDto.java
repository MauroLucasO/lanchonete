package br.edu.ifsudestemg.lanchonete.api.dto;

import br.edu.ifsudestemg.lanchonete.model.entity.Categoria;
import br.edu.ifsudestemg.lanchonete.model.entity.Estabelecimento;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class EstabelecimentoDto {

    private Long id;
    private String nome;
    private Long idProprietario;
    private String nomeProprietario;

    public static EstabelecimentoDto create(Estabelecimento estabelecimento) {
        ModelMapper modelMapper = new ModelMapper();
        EstabelecimentoDto dto = modelMapper.map(estabelecimento, EstabelecimentoDto.class);
        dto.nomeProprietario = estabelecimento.getProprietario().getNome();
        return dto;
    }
}
