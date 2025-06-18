package br.edu.ufape.sguPraeService.servicos.interfaces;

import br.edu.ufape.sguPraeService.exceptions.TipoAuxilioNotFoundException;
import br.edu.ufape.sguPraeService.models.TipoAuxilio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface TipoAuxilioService {
    Page<TipoAuxilio> listar(Pageable pageable);

    TipoAuxilio buscar(Long id) throws TipoAuxilioNotFoundException;

    TipoAuxilio salvar(TipoAuxilio entity);

    TipoAuxilio editar(Long id, TipoAuxilio entity) throws TipoAuxilioNotFoundException;

    void deletar(Long id) throws TipoAuxilioNotFoundException;

    void desativar(Long id) throws TipoAuxilioNotFoundException;
}
