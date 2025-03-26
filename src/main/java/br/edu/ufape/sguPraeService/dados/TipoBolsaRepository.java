package br.edu.ufape.sguPraeService.dados;

import br.edu.ufape.sguPraeService.models.TipoBolsa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TipoBolsaRepository extends JpaRepository<TipoBolsa, Long> {
}