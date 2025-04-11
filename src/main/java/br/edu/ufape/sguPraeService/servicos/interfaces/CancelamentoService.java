package br.edu.ufape.sguPraeService.servicos.interfaces;

import br.edu.ufape.sguPraeService.models.CancelamentoAgendamento;

import java.util.List;

public interface CancelamentoService {
    CancelamentoAgendamento salvar(CancelamentoAgendamento entity);

    CancelamentoAgendamento buscar(Long id);

    List<CancelamentoAgendamento> listar();

    List<CancelamentoAgendamento> ListarCancelamentosPorEstudante(String userId);

    List<CancelamentoAgendamento> ListarCancelamentosPorProfissional(String userId);

    List<CancelamentoAgendamento> ListarPorEstudanteAtual();

    List<CancelamentoAgendamento> ListarPorProfissionalAtual();
}
