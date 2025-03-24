package br.edu.ufape.sguPraeService.comunicacao.dto.profissional;

import br.edu.ufape.sguPraeService.models.Profissional;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class ProfissionalRequest {

    @NotBlank(message = "Especialidade é obrigatória")
    private String especialidade;

    public Profissional convertToEntity(ProfissionalRequest profissionalRequest, ModelMapper modelMapper) {
                return modelMapper.map(profissionalRequest, Profissional.class);
            }
}
