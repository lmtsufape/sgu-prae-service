package br.edu.ufape.sguPraeService.comunicacao.dto.tipoatendimento;

import br.edu.ufape.sguPraeService.models.TipoAtendimento;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import java.time.LocalTime;
import java.util.List;

@Getter @Setter
public class TipoAtendimentoRequest {

    @NotBlank(message = "Nome é obrigatório")
    private String nome;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    @NotNull(message = "Tempo de atendimento é obrigatório")
    private LocalTime tempoAtendimento;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    @NotEmpty(message = "Horários são obrigatórios")
    private List<LocalTime> horarios;


    public TipoAtendimento convertToEntity(TipoAtendimentoRequest tipoatendimentoRequest, ModelMapper modelMapper) {
        this.horarios = this.horarios.stream()
                .sorted()
                .toList();
        return modelMapper.map(tipoatendimentoRequest, TipoAtendimento.class);
            }
}
