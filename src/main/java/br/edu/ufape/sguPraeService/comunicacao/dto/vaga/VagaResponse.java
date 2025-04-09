package br.edu.ufape.sguPraeService.comunicacao.dto.vaga;

import br.edu.ufape.sguPraeService.models.Vaga;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import java.time.LocalTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class VagaResponse {

    Long id;

    LocalTime horaInicio;

    LocalTime horaFim;

    boolean disponivel;

    public VagaResponse(Vaga vaga, ModelMapper modelMapper){
        if (vaga == null) throw new IllegalArgumentException("Vaga não pode ser nulo");
        else modelMapper.map(vaga, this);
    }
}
