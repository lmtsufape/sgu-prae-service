package br.edu.ufape.sguPraeService.models;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
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

    @ManyToOne(optional = true)
    private TipoAuxilio tipoAuxilio;

    @ManyToOne(optional = true)
    private TipoBolsa tipoBolsa;

    private int horasBolsa;
    private LocalDate inicioBolsa;
    private LocalDate fimBolsa;
    private String parecerTermino;
    private BigDecimal valorBolsa;
    private boolean status = true;
    private boolean ativo = true;

    @ManyToMany(mappedBy = "auxilios")
    private List<Estudante> estudantes = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private Documento termo;

    @OneToMany(mappedBy="auxilio", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Pagamento> pagamentos = new ArrayList<>();

    public void addPagamento(Pagamento pagamento) {
    	pagamento.setAuxilio(this);
        pagamentos.add(pagamento);
    }

    public void addEstudante(Estudante estudante) {
        estudante.addAuxilio(this);
        estudantes.add(estudante);
    }
}
