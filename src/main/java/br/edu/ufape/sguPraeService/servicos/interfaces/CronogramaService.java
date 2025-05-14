package br.edu.ufape.sguPraeService.servicos.interfaces;

import br.edu.ufape.sguPraeService.exceptions.notFoundExceptions.CronogramaNotFoundException;
import br.edu.ufape.sguPraeService.models.Cronograma;

import java.util.List;
import java.util.UUID;

public interface CronogramaService {
    List<Cronograma> listar();

    List<Cronograma> listarPorTipoAtendimento(Long id);

    List<Cronograma> listarPorProfissional(UUID userId);

    Cronograma buscar(Long id) throws CronogramaNotFoundException;

    Cronograma salvar(Cronograma entity);

    void deletar(Long id);
}
