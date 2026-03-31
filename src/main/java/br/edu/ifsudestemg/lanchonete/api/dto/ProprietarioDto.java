package br.edu.ifsudestemg.lanchonete.api.dto;

import br.edu.ifsudestemg.lanchonete.model.entity.Proprietario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class ProprietarioDto {

    private Long id;
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

    public static ProprietarioDto create(Proprietario proprietario) {
        ModelMapper modelMapper = new ModelMapper();
        ProprietarioDto dto = modelMapper.map(proprietario, ProprietarioDto.class);
        return dto;
    }
}