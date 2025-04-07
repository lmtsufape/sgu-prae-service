package br.edu.ufape.sguPraeService.comunicacao.dto.estudante;

import br.edu.ufape.sguPraeService.models.DadosBancarios;
import br.edu.ufape.sguPraeService.models.Endereco;
import br.edu.ufape.sguPraeService.models.Estudante;
import br.edu.ufape.sguPraeService.models.TipoEtnia;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EstudanteRequest {
    private BigDecimal rendaPercapta;

    @NotNull(message = "O contato da família é obrigatório.")
    private String contatoFamilia;

    private boolean deficiente;

    private String tipoDeficiencia;

    private TipoEtnia tipoEtnia;

    private Endereco endereco;

    private DadosBancarios dadosBancarios;

    public Estudante convertToEntity(EstudanteRequest estudanteRequest, ModelMapper modelMapper) {
        return modelMapper.map(estudanteRequest, Estudante.class);
    }
}
