package br.edu.ufape.sguPraeService.comunicacao.dto.beneficio;

import br.edu.ufape.sguPraeService.models.enums.MotivoEncerramento;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class BeneficioCancelamentoRequest {

    @NotNull(message = "O motivo do encerramento é obrigatório")
    private MotivoEncerramento motivoEncerramento;

    private String parecerTermino;
}