package br.edu.ufape.sguPraeService.dados;

import br.edu.ufape.sguPraeService.models.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {
}