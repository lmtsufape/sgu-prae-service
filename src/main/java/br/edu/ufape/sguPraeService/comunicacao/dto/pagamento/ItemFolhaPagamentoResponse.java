package br.edu.ufape.sguPraeService.comunicacao.dto.pagamento;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import java.math.BigDecimal;
import java.util.UUID;

@Data
public class ItemFolhaPagamentoResponse {
    @JsonIgnore
    private UUID userId;

    private String nomeEstudante;
    private String cpf;
    private String matricula;

    // Dados Financeiros
    private String banco;
    private String agencia;
    private String conta;
    private String tipoConta;

    private BigDecimal valorTotalRecebido;
    private Integer quantidadeBeneficios;
    private String descricoesBeneficios;
}