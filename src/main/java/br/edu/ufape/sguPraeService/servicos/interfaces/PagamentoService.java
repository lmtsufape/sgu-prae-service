package br.edu.ufape.sguPraeService.servicos.interfaces;

import br.edu.ufape.sguPraeService.exceptions.notFoundExceptions.PagamentoNotFoundException;
import br.edu.ufape.sguPraeService.models.Pagamento;

import java.util.List;

public interface PagamentoService {
    List<Pagamento> listar();

    Pagamento buscar(Long id) throws PagamentoNotFoundException;

    Pagamento salvar(Pagamento entity);

    Pagamento editar(Long id, Pagamento entity) throws PagamentoNotFoundException;

    void deletar(Long id) throws PagamentoNotFoundException;
}
