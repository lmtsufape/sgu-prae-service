package br.edu.ufape.sguPraeService.servicos.interfaces;

import br.edu.ufape.sguPraeService.exceptions.notFoundExceptions.AgendamentoNotFoundException;
import br.edu.ufape.sguPraeService.models.Agendamento;
import br.edu.ufape.sguPraeService.models.Estudante;
import br.edu.ufape.sguPraeService.models.Profissional;
import br.edu.ufape.sguPraeService.models.Vaga;
import br.edu.ufape.sguPraeService.models.enums.ModalidadeAgendamento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AgendamentoService {

    Agendamento salvar(Agendamento entity);

    Agendamento agendar(Vaga vaga, Estudante estudante, ModalidadeAgendamento modalidade);

    Agendamento buscar(Long id) throws AgendamentoNotFoundException;

    Agendamento editar(Long Id, Agendamento entity) throws AgendamentoNotFoundException;

    void deletar(Long id);

    Page<Agendamento> listarAgendamentosPorEstudante(Estudante estudante, Pageable pageable);

    Page<Agendamento> listarAgendamentosEstudanteAtual(Pageable pageable);

    Page<Agendamento> listarPorProfissional(Profissional profissional, Pageable pageable);

    Page<Agendamento> listarPorProfissionalAtual(Pageable pageable);
}
