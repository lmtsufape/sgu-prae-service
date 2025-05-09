package br.edu.ufape.sguPraeService.comunicacao.dto.auxilio;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.modelmapper.ModelMapper;
import org.springframework.web.multipart.MultipartFile;

import br.edu.ufape.sguPraeService.comunicacao.annotations.AuxilioValido;
import br.edu.ufape.sguPraeService.models.Auxilio;
import br.edu.ufape.sguPraeService.models.TipoAuxilio;
import br.edu.ufape.sguPraeService.models.TipoBolsa;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AuxilioValido
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuxilioRequest {
    @Positive(message = "Id do tipo de auxílio inválido")
    private Long tipoAuxilioId;

    @Positive(message = "Id do tipo de bolsa inválido")
    private Long tipoBolsaId;

    @NotNull(message = "A quantidade de horas da bolsas é obrigatória")
    @Positive(message = "Quantidade de horas inválida")
    private Integer horasBolsa;
    @NotNull(message = "A data de início da bolsa é obrigatória")
    private LocalDate inicioBolsa;
    @NotNull(message = "A data de fim da bolsa é obrigatória")
    private LocalDate fimBolsa;

    @NotNull(message = "O valor da bolsa é obrigatório")
    @Positive(message = "Valor da bolsa inválido")
    private BigDecimal valorBolsa;

    @NotNull(message = "O termo não pode ser nulo")
    private MultipartFile termo;

    @NotNull(message = "O id do estudante é obrigatória")
    @Positive(message = "Id do estudante inválido")
    private Long estudanteId;

    public Auxilio convertToEntity(AuxilioRequest auxilioRequest, ModelMapper modelMapper) {
        Auxilio auxilio = modelMapper.map(auxilioRequest, Auxilio.class);
        if (tipoAuxilioId != null) {
            TipoAuxilio tipoAuxilio = new TipoAuxilio();
            tipoAuxilio.setId(tipoAuxilioId);
            auxilio.setTipoAuxilio(tipoAuxilio);
        }

        if (tipoBolsaId != null) {
            TipoBolsa tipoBolsa = new TipoBolsa();
            tipoBolsa.setId(tipoBolsaId);
            auxilio.setTipoBolsa(tipoBolsa);
        }
        return auxilio;
    }
}
