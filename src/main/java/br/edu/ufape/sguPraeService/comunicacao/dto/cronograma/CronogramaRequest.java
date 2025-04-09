package br.edu.ufape.sguPraeService.comunicacao.dto.cronograma;

import br.edu.ufape.sguPraeService.models.Cronograma;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import java.time.LocalDate;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class CronogramaRequest {

    @JsonFormat(pattern = "dd/MM/yyyy")
    @NotNull(message = "Data é obrigatória")
    private LocalDate data;

    @NotNull(message = "Tipo de atendimento é obrigatório")
    private Long tipoAtendimentoId;

    public Cronograma convertToEntity(CronogramaRequest cronogramaRequest, ModelMapper modelMapper) {
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT);
        return modelMapper.map(cronogramaRequest, Cronograma.class);
            }
}
