package br.edu.ufape.sguPraeService.servicos.interfaces;

import br.edu.ufape.sguPraeService.exceptions.TipoBolsaNotFoundException;
import br.edu.ufape.sguPraeService.models.TipoBolsa;

import java.util.List;

public interface TipoBolsaService {
    List<TipoBolsa> listar();

    TipoBolsa buscar(Long id) throws TipoBolsaNotFoundException;

    TipoBolsa salvar(TipoBolsa entity);

    TipoBolsa editar(Long id, TipoBolsa entity) throws TipoBolsaNotFoundException;

    void deletar(Long id) throws TipoBolsaNotFoundException;
}
