package br.edu.ufape.sguPraeService.comunicacao.dto.vaga;

import br.edu.ufape.sguPraeService.comunicacao.annotations.ConsistenteHorario;
import br.edu.ufape.sguPraeService.models.Vaga;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import java.time.LocalTime;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor @ConsistenteHorario
public class VagaRequest {

    @NotNull(message = "O campo 'horaInicio' é obrigatório.")
    private LocalTime horaInicio;

    @NotNull(message = "O campo 'horaFim' é obrigatório.")
    private LocalTime horaFim;

    public Vaga convertToEntity(VagaRequest vagaRequest, ModelMapper modelMapper) {
                return modelMapper.map(vagaRequest, Vaga.class);
            }
}
