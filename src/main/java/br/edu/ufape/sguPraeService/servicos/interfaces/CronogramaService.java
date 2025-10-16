package br.edu.ufape.sguPraeService.servicos.interfaces;

import br.edu.ufape.sguPraeService.exceptions.notFoundExceptions.CronogramaNotFoundException;
import br.edu.ufape.sguPraeService.models.Cronograma;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.util.UUID;

public interface CronogramaService {
    Page<Cronograma> listar(Pageable pageable);

    Page<Cronograma> listarPorTipoAtendimento(Long id, Pageable pageable);

    Page<Cronograma> listarPorProfissional(UUID userId, Pageable pageable);

    Cronograma buscar(Long id) throws CronogramaNotFoundException;

    Cronograma salvar(Cronograma entity);

    boolean existeCronogramaPorTipoAtendimento(Long tipoAtendimentoId);

    void deletar(Long id);
}
