package br.edu.ufape.sguPraeService.comunicacao.dto.pagamento;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PagamentoPatchRequest {
    private BigDecimal valor;
    private LocalDate data;
    private Boolean ativo;
}