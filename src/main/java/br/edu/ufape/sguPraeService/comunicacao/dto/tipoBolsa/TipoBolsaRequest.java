package br.edu.ufape.sguPraeService.comunicacao.dto.tipoBolsa;

import br.edu.ufape.sguPraeService.models.TipoBolsa;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class TipoBolsaRequest {

    @NotBlank(message = "Descrição é obrigatória")
    @NotNull(message = "Descrição não pode ser nula")
    @Size(max = 100, message = "Descrição não pode ter mais de 100 caracteres")
    private String descricao;

    public TipoBolsa convertToEntity(TipoBolsaRequest tipoBolsaRequest, ModelMapper modelMapper) {
        return modelMapper.map(tipoBolsaRequest, TipoBolsa.class);
    }
}