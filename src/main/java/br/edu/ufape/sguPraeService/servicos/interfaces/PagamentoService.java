package br.edu.ufape.sguPraeService.servicos.interfaces;

import br.edu.ufape.sguPraeService.exceptions.notFoundExceptions.PagamentoNotFoundException;
import br.edu.ufape.sguPraeService.models.Pagamento;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PagamentoService {
    List<Pagamento> listar();

    Page<Pagamento> listar(Pageable pageable);

    Pagamento buscar(Long id) throws PagamentoNotFoundException;

    List<Pagamento> salvar(List<Pagamento> pagamentos);

    Pagamento editar(Long id, Pagamento entity) throws PagamentoNotFoundException;

    void deletar(Long id) throws PagamentoNotFoundException;

    void desativar(Long id) throws PagamentoNotFoundException;

    List<Pagamento> listarPorValor(BigDecimal min, BigDecimal max);

    Page<Pagamento> listarPorValor(BigDecimal min, BigDecimal max, Pageable pageable);

    List<Pagamento> listarPorEstudanteId(Long estudanteId);
}
