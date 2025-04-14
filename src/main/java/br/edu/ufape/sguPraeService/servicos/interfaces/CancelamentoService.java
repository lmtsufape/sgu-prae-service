package br.edu.ufape.sguPraeService.servicos.interfaces;

import br.edu.ufape.sguPraeService.models.CancelamentoAgendamento;

import java.util.List;
import java.util.UUID;

public interface CancelamentoService {
    CancelamentoAgendamento salvar(CancelamentoAgendamento entity);

    CancelamentoAgendamento buscar(Long id);

    List<CancelamentoAgendamento> listar();

    List<CancelamentoAgendamento> ListarCancelamentosPorEstudante(UUID userId);

    List<CancelamentoAgendamento> ListarCancelamentosPorProfissional(UUID userId);

    List<CancelamentoAgendamento> ListarPorEstudanteAtual();

    List<CancelamentoAgendamento> ListarPorProfissionalAtual();
}
