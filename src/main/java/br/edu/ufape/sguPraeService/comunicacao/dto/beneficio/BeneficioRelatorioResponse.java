package br.edu.ufape.sguPraeService.comunicacao.dto.beneficio;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
public class BeneficioRelatorioResponse {
    private Long auxilioId;
    private String descricaoBeneficio;
    private BigDecimal valorBeneficio;
    private List<PagamentoRelatorioResponse> pagamentos;
    private BigDecimal totalGasto;
    private List<EstudanteRelatorioResponse> estudantes;
}