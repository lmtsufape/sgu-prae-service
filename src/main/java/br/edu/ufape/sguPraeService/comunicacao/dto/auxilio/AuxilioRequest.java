package br.edu.ufape.sguPraeService.comunicacao.dto.auxilio;
import br.edu.ufape.sguPraeService.models.Auxilio;
import br.edu.ufape.sguPraeService.models.TipoBolsa;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class AuxilioRequest {

    @NotBlank(message = "O tipo da bolsas é obrigatória")
    private TipoBolsa tipoBolsa;
    @NotBlank(message = "A quantidade de horas da bolsas é obrigatória")
    private Integer horasBolsa;
    @NotBlank(message = "A data de início da bolsa é obrigatória")
    private LocalDate inicioBolsa;
    @NotBlank(message = "A data de fim da bolsa é obrigatória")
    private LocalDate fimBolsa;
    @NotBlank(message = "O valor da bolsa é obrigatório")
    private BigDecimal valorBolsa;


    public Auxilio convertToEntity(AuxilioRequest auxilioRequest, ModelMapper modelMapper) {
        return modelMapper.map(auxilioRequest, Auxilio.class);
    }
}
