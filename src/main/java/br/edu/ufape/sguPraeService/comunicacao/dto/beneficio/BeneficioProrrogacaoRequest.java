package br.edu.ufape.sguPraeService.comunicacao.dto.beneficio;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.YearMonth;

@Data
public class BeneficioProrrogacaoRequest {

    @NotNull(message = "O novo prazo (mês/ano) é obrigatório para a prorrogação.")
    private YearMonth novoPrazo;

    private String observacoes;
}