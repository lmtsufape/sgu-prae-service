package br.edu.ufape.sguPraeService.servicos.interfaces;

import br.edu.ufape.sguPraeService.exceptions.notFoundExceptions.CronogramaNotFoundException;
import br.edu.ufape.sguPraeService.models.Cronograma;

import java.util.List;

public interface CronogramaService {
    List<Cronograma> listar();

    List<Cronograma> listarPorTipoAtendimento(Long id);

    List<Cronograma> listarPorProfissional(String userId);

    Cronograma buscar(Long id) throws CronogramaNotFoundException;

    Cronograma salvar(Cronograma entity);

    Cronograma editar(Long id, Cronograma entity) throws CronogramaNotFoundException;

    void deletar(Long id);
}
