package br.edu.ufape.sguPraeService.servicos.interfaces;

import br.edu.ufape.sguPraeService.exceptions.notFoundExceptions.PagamentoNotFoundException;
import br.edu.ufape.sguPraeService.models.Pagamento;

import java.math.BigDecimal;
import java.util.List;

public interface PagamentoService {
    List<Pagamento> listar();

    Pagamento buscar(Long id) throws PagamentoNotFoundException;

    List<Pagamento> salvar(List<Pagamento> pagamentos);

    Pagamento editar(Long id, Pagamento entity) throws PagamentoNotFoundException;

    void deletar(Long id) throws PagamentoNotFoundException;

    void desativar(Long id) throws PagamentoNotFoundException;

    List<Pagamento> listarPorValor(BigDecimal min, BigDecimal max);
}
