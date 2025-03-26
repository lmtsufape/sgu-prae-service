package br.edu.ufape.sguPraeService.servicos.interfaces;

import br.edu.ufape.sguPraeService.exceptions.TipoAuxilioNotFoundException;
import br.edu.ufape.sguPraeService.models.TipoAuxilio;

import java.util.List;

public interface TipoAuxilioService {
    List<TipoAuxilio> listar();

    TipoAuxilio buscar(Long id) throws TipoAuxilioNotFoundException;

    TipoAuxilio salvar(TipoAuxilio entity);

    TipoAuxilio editar(Long id, TipoAuxilio entity) throws TipoAuxilioNotFoundException;

    void deletar(Long id) throws TipoAuxilioNotFoundException;
}
