package br.edu.ufape.sguPraeService.models;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class Beneficio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne()
    private TipoBeneficio tipoBeneficio;

    private int horasBeneficio;
    private LocalDate inicioBeneficio;
    private LocalDate fimBeneficio;
    private String parecerTermino;
    private BigDecimal valorPagamento;
    private boolean status = true;
    private boolean ativo = true;

    @ManyToOne
    private Estudante estudantes;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private Documento termo;

    @OneToMany(mappedBy= "beneficio", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    private List<Pagamento> pagamentos = new ArrayList<>();

    public void addPagamento(Pagamento pagamento) {
    	pagamento.setBeneficio(this);
        pagamentos.add(pagamento);
    }

}
