package br.edu.ufape.sguPraeService.comunicacao.dto.pagamento;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class FolhaPagamentoResponse {
    private Integer anoReferencia;
    private Integer mesReferencia;
    private String numeroLote;

    // Totais calculados
    private BigDecimal valorTotalGeral;
    private Integer quantidadeTotalPagamentos;
    private Integer quantidadeEstudantes;

    // Lista detalhada por estudante
    private List<ItemFolhaPagamentoResponse> itens;
}