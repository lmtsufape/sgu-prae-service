package br.edu.ufape.sguPraeService.comunicacao.dto.tipoBeneficio;
import br.edu.ufape.sguPraeService.models.TipoBeneficio;
import br.edu.ufape.sguPraeService.models.enums.NaturezaBeneficio;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class TipoBeneficioRequest {

    @NotBlank(message = "Descrição é obrigatório")
    @Size(min = 3, max = 50, message = "Descrição deve ter entre 3 e 50 caracteres")
    private String tipo;
    @NotNull(message = "Natureza do benefício é obrigatória")
    private NaturezaBeneficio naturezaBeneficio;
    @Size(max = 200, message = "Descrição deve ter no máximo 200 caracteres")
    private String descricao;
    @PositiveOrZero(message = "Valor do beneficio inválido")
    @NotNull(message = "Valor do beneficio é obrigatório")
    private BigDecimal valorBeneficio;

    public TipoBeneficio convertToEntity(TipoBeneficioRequest tipoBeneficioRequest, ModelMapper modelMapper) {
        return modelMapper.map(tipoBeneficioRequest, TipoBeneficio.class);
    }
}
