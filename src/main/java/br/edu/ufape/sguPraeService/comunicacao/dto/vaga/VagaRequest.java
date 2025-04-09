package br.edu.ufape.sguPraeService.comunicacao.dto.vaga;

import br.edu.ufape.sguPraeService.models.Vaga;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import java.time.LocalTime;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class VagaRequest {

    private LocalTime horaInicio;

    private LocalTime horaFim;

    public Vaga convertToEntity(VagaRequest vagaRequest, ModelMapper modelMapper) {
                return modelMapper.map(vagaRequest, Vaga.class);
            }
}
