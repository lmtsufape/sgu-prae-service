package br.edu.ufape.sguPraeService.servicos.interfaces;

import br.edu.ufape.sguPraeService.exceptions.notFoundExceptions.AgendamentoNotFoundException;
import br.edu.ufape.sguPraeService.models.Agendamento;
import br.edu.ufape.sguPraeService.models.Estudante;
import br.edu.ufape.sguPraeService.models.Profissional;
import br.edu.ufape.sguPraeService.models.Vaga;

import java.util.List;

public interface AgendamentoService {

    Agendamento salvar(Agendamento entity);

    Agendamento agendar(Vaga vaga, Estudante estudante);

    Agendamento buscar(Long id) throws AgendamentoNotFoundException;

    Agendamento editar(Long Id, Agendamento entity) throws AgendamentoNotFoundException;

    void deletar(Long id);

    List<Agendamento> listarAgendamentosPorEstudante(Estudante estudante);

    List<Agendamento> listarAgendamentosEstudanteAtual();

    List<Agendamento> listarPorProfissional(Profissional profissional);

    List<Agendamento> listarPorProfissionalAtual();
}
