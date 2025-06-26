package br.edu.ufape.sguPraeService.comunicacao.dto.estudante;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RelatorioBeneficioResponse {
        private String tipoAuxilio;
        private BigDecimal valorBolsa;
        private LocalDate inicioBolsa;
        private LocalDate fimBolsa;
}
