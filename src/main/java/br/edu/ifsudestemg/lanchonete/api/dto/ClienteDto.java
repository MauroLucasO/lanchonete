package br.edu.ifsudestemg.lanchonete.api.dto;

import br.edu.ifsudestemg.lanchonete.model.entity.Cliente;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class ClienteDto {

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

    public static ClienteDto create(Cliente cliente) {
        ModelMapper modelMapper = new ModelMapper();
        ClienteDto dto = modelMapper.map(cliente, ClienteDto.class);
        return dto;
    }
}