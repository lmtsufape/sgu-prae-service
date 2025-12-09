package br.edu.ufape.sguPraeService.comunicacao.dto.pagamento;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CPF;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter @Setter
public class PagamentoCPFRequest {
    @CPF(message = "CPF inválido")
    @NotBlank(message = "O CPF é obrigatório")
    private String cpf;

    @NotNull(message = "O valor é obrigatório")
    @Positive(message = "O valor deve ser positivo")
    private BigDecimal valor;

    @NotBlank(message = "O número do lote é obrigatório")
    private String numeroLote;

    @NotNull(message = "A data do pagamento é obrigatória")
    private LocalDate data;

    @NotNull(message = "O mês de referência é obrigatório")
    private Integer mesReferencia;

    @NotNull(message = "O ano de referência é obrigatório")
    private Integer anoReferencia;

    // Necessário apenas para alunos com mais de um benefício ativo
    private Long TipoBeneficioId;
}