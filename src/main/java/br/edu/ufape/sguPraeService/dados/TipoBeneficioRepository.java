package br.edu.ufape.sguPraeService.dados;

import br.edu.ufape.sguPraeService.models.TipoBeneficio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TipoBeneficioRepository extends JpaRepository<TipoBeneficio, Long> {
}