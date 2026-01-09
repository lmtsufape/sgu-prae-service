package br.edu.ufape.sguPraeService.comunicacao.dto.agendamento;

import br.edu.ufape.sguPraeService.models.enums.ModalidadeAgendamento;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AgendamentoRequest {
    @NotNull(message = "O ID da vaga é obrigatório")
    private Long vagaId;

}