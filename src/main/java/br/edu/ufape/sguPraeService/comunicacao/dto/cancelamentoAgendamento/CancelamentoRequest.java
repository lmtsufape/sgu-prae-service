package br.edu.ufape.sguPraeService.comunicacao.dto.cancelamentoAgendamento;

import br.edu.ufape.sguPraeService.models.CancelamentoAgendamento;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class CancelamentoRequest {

    @Size(max = 255, message = "O motivo do cancelamento deve ter no máximo 255 caracteres.")
    private String motivo;

    public CancelamentoAgendamento convertToEntity(CancelamentoRequest cancelamentoRequest, ModelMapper modelMapper) {
        return modelMapper.map(cancelamentoRequest, CancelamentoAgendamento.class);
    }
}
