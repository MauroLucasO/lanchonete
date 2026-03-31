package br.edu.ifsudestemg.lanchonete.api.dto;

import br.edu.ifsudestemg.lanchonete.model.entity.Categoria;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class CategoriaDto {

    private Long id;
    private String nome;

    public static CategoriaDto create(Categoria categoria) {
        ModelMapper modelMapper = new ModelMapper();
        CategoriaDto dto = modelMapper.map(categoria, CategoriaDto.class);
        return dto;
    }
}
