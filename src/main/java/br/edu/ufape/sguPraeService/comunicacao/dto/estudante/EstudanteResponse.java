package br.edu.ufape.sguPraeService.comunicacao.dto.estudante;

import br.edu.ufape.sguPraeService.comunicacao.dto.beneficio.BeneficioResponse;
import br.edu.ufape.sguPraeService.comunicacao.dto.dadosBancarios.DadosBancariosResponse;
import br.edu.ufape.sguPraeService.comunicacao.dto.endereco.EnderecoResponse;
import br.edu.ufape.sguPraeService.comunicacao.dto.tipoEtnia.TipoEtniaResponse;
import br.edu.ufape.sguPraeService.comunicacao.dto.usuario.AlunoResponse;
import br.edu.ufape.sguPraeService.models.Estudante;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class EstudanteResponse {

    private Long id;
    private AlunoResponse aluno;
    private BigDecimal rendaPercapta;
    private String contatoFamilia;
    private boolean deficiente;
    private String tipoDeficiencia;
    private TipoEtniaResponse tipoEtnia;
    private EnderecoResponse endereco;
    private DadosBancariosResponse dadosBancarios;

    public EstudanteResponse(Estudante estudante,  ModelMapper modelMapper) {
        if (estudante == null) throw new IllegalArgumentException("Estudante não pode ser nulo");
        modelMapper.map(estudante, this);
    }
}
