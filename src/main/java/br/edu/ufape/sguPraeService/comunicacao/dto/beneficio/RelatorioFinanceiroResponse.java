package br.edu.ufape.sguPraeService.comunicacao.dto.beneficio;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public class RelatorioFinanceiroResponse {
    private BigDecimal totalGeral;
    private Long quantidadePessoasAtendidas;
    private Long quantidadeTiposBeneficio;
    private Long quantidadeCursosDistintos;
    private List<Map<String, Object>> valorTotalPorTipoBeneficio;
    private List<Map<String, Object>> quantidadeBeneficiadosPorCurso;
}