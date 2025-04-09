package br.edu.ufape.sguPraeService.dados;

import br.edu.ufape.sguPraeService.models.Vaga;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VagaRepository extends JpaRepository<Vaga, Long> {
}
