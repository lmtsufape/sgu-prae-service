package br.edu.ufape.sguPraeService.comunicacao.dto.tipoBolsa;

import br.edu.ufape.sguPraeService.models.TipoBolsa;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter @Setter @NoArgsConstructor
public class TipoBolsaResponse {

    Long id;
    String descricao;

    public TipoBolsaResponse(TipoBolsa tipoBolsa, ModelMapper modelMapper){
        if (tipoBolsa == null) throw new IllegalArgumentException("Tipo Bolsa não pode ser nulo");
        else modelMapper.map(tipoBolsa, this);
    }
}
