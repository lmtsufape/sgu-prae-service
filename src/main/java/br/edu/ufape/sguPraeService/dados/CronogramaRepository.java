package br.edu.ufape.sguPraeService.dados;

import br.edu.ufape.sguPraeService.models.Cronograma;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CronogramaRepository extends JpaRepository<Cronograma, Long> {
    List<Cronograma> findByTipoAtendimentoId(Long id);

    List<Cronograma> findByProfissional_UserId(String profissionalUserId);
}
