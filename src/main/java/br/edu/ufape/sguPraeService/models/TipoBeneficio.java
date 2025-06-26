package br.edu.ufape.sguPraeService.models;

import br.edu.ufape.sguPraeService.models.enums.NaturezaBeneficio;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class TipoBeneficio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tipo;
    private NaturezaBeneficio naturezaBeneficio;
    private String descricao;
    private BigDecimal valorBeneficio;
    private boolean ativo = true;
}
