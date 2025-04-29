package br.edu.ufape.sguPraeService.models;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

    @ManyToOne
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

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private Documento termo;
    
    @OneToMany
    private List<Pagamento> pagamentos= new ArrayList<>();
    
    public void addPagamento(Pagamento pagamento) {
    	pagamentos.add(pagamento);
    }
}
