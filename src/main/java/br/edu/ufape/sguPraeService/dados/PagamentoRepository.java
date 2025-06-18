package br.edu.ufape.sguPraeService.dados;

import br.edu.ufape.sguPraeService.models.Pagamento;

import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {
    List<Pagamento> findByValorBetween(BigDecimal min, BigDecimal max);
    Page<Pagamento> findByValorBetween(BigDecimal min, BigDecimal max, Pageable pageable);
}