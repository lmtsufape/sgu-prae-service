package br.edu.ufape.sguPraeService.comunicacao.dto.tipoatendimento;

import br.edu.ufape.sguPraeService.models.TipoAtendimento;

import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import java.time.LocalTime;
import java.util.List;

@Getter @Setter
public class TipoAtendimentoResponse {

    private Long id;
    private String nome;
    private LocalTime tempoAtendimento;
    private List<LocalTime> horarios;

    public TipoAtendimentoResponse(TipoAtendimento tipoatendimento, ModelMapper modelMapper){
        if (tipoatendimento == null) throw new IllegalArgumentException("TipoAtendimento não pode ser nulo");
        else modelMapper.map(tipoatendimento, this);
    }
}
