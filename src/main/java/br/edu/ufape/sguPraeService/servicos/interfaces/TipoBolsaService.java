package br.edu.ufape.sguPraeService.servicos.interfaces;

import br.edu.ufape.sguPraeService.exceptions.TipoBolsaNotFoundException;
import br.edu.ufape.sguPraeService.models.TipoBolsa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TipoBolsaService {
    Page<TipoBolsa> listar(Pageable pageable);

    TipoBolsa buscar(Long id) throws TipoBolsaNotFoundException;

    TipoBolsa salvar(TipoBolsa entity);

    TipoBolsa editar(Long id, TipoBolsa entity) throws TipoBolsaNotFoundException;

    void deletar(Long id) throws TipoBolsaNotFoundException;

    void desativar(Long id) throws TipoBolsaNotFoundException;
}
