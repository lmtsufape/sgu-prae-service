package br.edu.ufape.sguPraeService.comunicacao.dto.auxilio;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
public class AuxilioRelatorioResponse {
    private Long auxilioId;
    private String descricaoBolsa;
    private BigDecimal totalGasto;
    private List<EstudanteRelatorioResponse> estudantes;
}