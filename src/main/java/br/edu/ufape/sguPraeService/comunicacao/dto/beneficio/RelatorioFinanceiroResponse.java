package br.edu.ufape.sguPraeService.comunicacao.dto.beneficio;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RelatorioFinanceiroResponse {
    private Double totalGeral;
    private Integer quantidadePessoasAtendidas;
    private Integer quantidadeTiposBeneficio;
    private Integer quantidadeCursosDistintos;

    private List<ValorPorTipoDTO> valorTotalPorTipoBeneficio;
    private List<BeneficiadosPorCursoDTO> quantidadeBeneficiadosPorCurso;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ValorPorTipoDTO {
        private Long tipoBeneficioId;
        private String descricao;
        private Double valorTotal;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BeneficiadosPorCursoDTO {
        private Long cursoId;
        private String cursoNome;
        private Long quantidadeBeneficiados;
    }
}