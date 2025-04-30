package br.edu.ufape.sguPraeService.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;


@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Estudante{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private BigDecimal rendaPercapta;
    private String contatoFamilia;
    private boolean deficiente = false;
    private String tipoDeficiencia;
    @Column(unique = true, nullable = false)
    private UUID userId;
    @OneToOne(cascade = CascadeType.ALL)
    private DadosBancarios dadosBancarios;
    private boolean ativo = true;

    @OneToOne(cascade = CascadeType.ALL)
    private Endereco endereco;

    @ManyToOne
    private TipoEtnia tipoEtnia;

    @ManyToMany
    @JoinTable(name = "estudante_auxilio",
                         joinColumns = @JoinColumn(name = "estudante_id"),
                         inverseJoinColumns = @JoinColumn(name = "auxilio_id")
    )
    private List<Auxilio> auxilios;
    
    public void addAuxilio(Auxilio auxilio) {
    	this.auxilios.add(auxilio);
    }
}
