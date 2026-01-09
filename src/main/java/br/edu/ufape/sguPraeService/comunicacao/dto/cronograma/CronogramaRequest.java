package br.edu.ufape.sguPraeService.comunicacao.dto.cronograma;

import br.edu.ufape.sguPraeService.models.Cronograma;

import br.edu.ufape.sguPraeService.models.enums.ModalidadeAgendamento;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class CronogramaRequest {

    @JsonFormat(pattern = "dd/MM/yyyy")
    @NotEmpty(message = "É origatória ao menos uma data")
    private List<LocalDate> datas;

    @NotNull(message = "Tipo de atendimento é obrigatório")
    private Long tipoAtendimentoId;

    @NotNull(message = "A modalidade de atendimento é obrigatória")
    private ModalidadeAgendamento modalidade;

    public List<Cronograma> convertToEntities(ModelMapper modelMapper) {
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT);

        return datas.stream()
                .map(data -> {
                    Cronograma cronograma = modelMapper.map(this, Cronograma.class);
                    cronograma.setData(data);
                    return cronograma;
                })
                .collect(Collectors.toList());
    }
}
