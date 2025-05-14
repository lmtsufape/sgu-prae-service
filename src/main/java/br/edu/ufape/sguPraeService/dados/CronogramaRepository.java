package br.edu.ufape.sguPraeService.dados;

import br.edu.ufape.sguPraeService.models.Cronograma;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CronogramaRepository extends JpaRepository<Cronograma, Long> {
    List<Cronograma> findAllByAtivoTrue();
    List<Cronograma> findByAtivoTrueAndTipoAtendimento_Id(Long id);

    List<Cronograma> findAllByAtivoTrueAndProfissional_UserId(UUID profissionalUserId);
}
