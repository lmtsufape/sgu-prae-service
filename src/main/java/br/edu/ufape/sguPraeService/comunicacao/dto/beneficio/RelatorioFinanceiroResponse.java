package br.edu.ufape.sguPraeService.comunicacao.dto.beneficio;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
public class RelatorioFinanceiroResponse {
    private List<BeneficioRelatorioResponse> detalhes;
    private BigDecimal totalGeral;
}