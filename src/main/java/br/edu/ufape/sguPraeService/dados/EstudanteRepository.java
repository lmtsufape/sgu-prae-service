package br.edu.ufape.sguPraeService.dados;

import br.edu.ufape.sguPraeService.models.Estudante;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EstudanteRepository extends JpaRepository<Estudante, Long> {
}
