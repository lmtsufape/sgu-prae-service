package br.edu.ufape.sguPraeService.servicos.interfaces;

import br.edu.ufape.sguPraeService.models.CancelamentoAgendamento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.util.UUID;

public interface CancelamentoService {
    CancelamentoAgendamento salvar(CancelamentoAgendamento entity);

    CancelamentoAgendamento buscar(Long id);

    Page<CancelamentoAgendamento> listar(Pageable pageable);

    Page<CancelamentoAgendamento> ListarCancelamentosPorEstudante(UUID userId, Pageable pageable);

    Page<CancelamentoAgendamento> ListarCancelamentosPorProfissional(UUID userId, Pageable pageable);

    Page<CancelamentoAgendamento> ListarPorEstudanteAtual(Pageable pageable);

    Page<CancelamentoAgendamento> ListarPorProfissionalAtual(Pageable pageable);
}
