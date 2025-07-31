package br.edu.ufape.sguPraeService.comunicacao.dto.estudante;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RelatorioEstudanteAssistidoResponse {
    private String nome;
    private BigDecimal rendaPercapta;
    private String contatoFamilia;
    private boolean deficiente;
    private String tipoDeficiencia;
    private List<RelatorioBeneficioResponse> auxilios;
}

