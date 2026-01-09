package br.edu.ufape.sguPraeService.comunicacao.dto.agendamento;

import br.edu.ufape.sguPraeService.comunicacao.dto.estudante.EstudanteResponse;
import br.edu.ufape.sguPraeService.comunicacao.dto.vaga.VagaResponse;
import br.edu.ufape.sguPraeService.models.Agendamento;
import br.edu.ufape.sguPraeService.models.enums.ModalidadeAgendamento;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class AgendamentoResponse {

    private Long id;

    private LocalDate data;

    private String tipoAtendimento;

    private VagaResponse vaga;

    private ModalidadeAgendamento modalidade;

    private EstudanteResponse estudante;

    public AgendamentoResponse(Agendamento agendamento, ModelMapper modelMapper) {
        if (agendamento == null) throw new IllegalArgumentException("Agendamento não pode ser nulo");
        modelMapper.map(agendamento, this);
        this.tipoAtendimento = agendamento.getVaga().getCronograma().getTipoAtendimento().getNome();
        this.modalidade = agendamento.getVaga().getCronograma().getModalidade();
    }

}
