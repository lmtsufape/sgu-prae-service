package br.edu.ufape.sguPraeService.comunicacao.dto.agendamento;

import br.edu.ufape.sguPraeService.models.enums.ModalidadeAgendamento;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AgendamentoModalidadeRequest {
    @NotNull(message = "A nova modalidade é obrigatória")
    private ModalidadeAgendamento modalidade;
}