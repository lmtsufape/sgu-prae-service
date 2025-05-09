package br.edu.ufape.sguPraeService.comunicacao.dto.auxilio;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class PagamentoRelatorioResponse {
    private BigDecimal valor;
    private LocalDate data;
}