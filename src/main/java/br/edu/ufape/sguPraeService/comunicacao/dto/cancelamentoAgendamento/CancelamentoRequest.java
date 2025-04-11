package br.edu.ufape.sguPraeService.comunicacao.dto.cancelamentoAgendamento;

import br.edu.ufape.sguPraeService.models.CancelamentoAgendamento;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class CancelamentoRequest {

    private String motivo;

    public CancelamentoAgendamento convertToEntity(CancelamentoRequest cancelamentoRequest, ModelMapper modelMapper) {
        return modelMapper.map(cancelamentoRequest, CancelamentoAgendamento.class);
    }
}
