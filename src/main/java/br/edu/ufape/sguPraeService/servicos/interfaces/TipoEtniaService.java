package br.edu.ufape.sguPraeService.servicos.interfaces;

import br.edu.ufape.sguPraeService.exceptions.notFoundExceptions.TipoEtniaNotFoundException;
import br.edu.ufape.sguPraeService.models.TipoEtnia;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface TipoEtniaService {
    TipoEtnia salvarTipoEtnia(TipoEtnia tipoEtnia);

    TipoEtnia buscarTipoEtnia(Long id) throws TipoEtniaNotFoundException;

    Page<TipoEtnia> listarTiposEtnia(Pageable pageable);

    TipoEtnia atualizarTipoEtnia(Long id, TipoEtnia tipoEtnia) throws TipoEtniaNotFoundException;

    void deletarTipoEtnia(Long id) throws TipoEtniaNotFoundException;
}