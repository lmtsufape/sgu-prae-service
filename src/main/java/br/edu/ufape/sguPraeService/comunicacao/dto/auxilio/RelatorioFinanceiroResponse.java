package br.edu.ufape.sguPraeService.comunicacao.dto.auxilio;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
public class RelatorioFinanceiroResponse {
    private List<AuxilioRelatorioResponse> detalhes;
    private BigDecimal totalGeral;
}