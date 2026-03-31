package br.edu.ifsudestemg.lanchonete.api.dto;

import br.edu.ifsudestemg.lanchonete.model.entity.Gerente;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class GerenteDto {

    private String nome;
    private String cpf;
    private String telefone;
    private String cep;
    private String cidade;
    private String estado;
    private String logradouro;
    private String email;
    private String senha;
    private Long idEstabelecimento;

    public static GerenteDto create(Gerente gerente) {
        ModelMapper modelMapper = new ModelMapper();
        GerenteDto dto = modelMapper.map(gerente, GerenteDto.class);
        return dto;
    }
}