package br.edu.ufape.sguPraeService.models;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class Auxilio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne()
    private TipoAuxilio tipoAuxilio;

    @ManyToOne()
    private TipoBolsa tipoBolsa;

    private int horasBolsa;
    private LocalDate inicioBolsa;
    private LocalDate fimBolsa;
    private String parecerTermino;
    private BigDecimal valorBolsa;
    private BigDecimal valorPagamento;
    private boolean status = true;
    private boolean ativo = true;

    @ManyToMany(mappedBy = "auxilios")
    @JsonBackReference
    private List<Estudante> estudantes = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private Documento termo;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "auxilio_id")
    private List<Pagamento> pagamentos = new ArrayList<>();

    public void addPagamento(Pagamento pagamento) {
        pagamentos.add(pagamento);
    }

    public void addEstudante(Estudante estudante) {
        estudante.addAuxilio(this);
        estudantes.add(estudante);
    }
}
