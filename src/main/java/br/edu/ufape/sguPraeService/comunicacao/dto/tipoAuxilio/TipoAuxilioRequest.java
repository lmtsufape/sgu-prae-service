package br.edu.ufape.sguPraeService.comunicacao.dto.tipoAuxilio;
import br.edu.ufape.sguPraeService.models.TipoAuxilio;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class TipoAuxilioRequest {

    @NotBlank(message = "Descrição é obrigatório")
    private String tipo;
    @PositiveOrZero(message = "Valor do auxílio inválido")
    @NotNull(message = "Valor do auxílio é obrigatório")
    private BigDecimal valorAuxilio;

    public TipoAuxilio convertToEntity(TipoAuxilioRequest tipoAuxilioRequest, ModelMapper modelMapper) {
        return modelMapper.map(tipoAuxilioRequest, TipoAuxilio.class);
    }
}
