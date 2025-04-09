package br.edu.ufape.sguPraeService.servicos.interfaces;

import br.edu.ufape.sguPraeService.exceptions.notFoundExceptions.TipoAtendimentoNotFoundException;
import br.edu.ufape.sguPraeService.models.TipoAtendimento;

import java.util.List;

public interface TipoAtendimentoService {
    List<TipoAtendimento> listar();

    TipoAtendimento buscar(Long id) throws TipoAtendimentoNotFoundException;

    TipoAtendimento salvar(TipoAtendimento entity);

    TipoAtendimento editar(Long id, TipoAtendimento entity) throws TipoAtendimentoNotFoundException;

    TipoAtendimento deletarHorario(Long id, int index) throws TipoAtendimentoNotFoundException;

    void deletar(Long id);
}
