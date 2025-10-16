package br.edu.ufape.sguPraeService.dados;

import br.edu.ufape.sguPraeService.models.Cronograma;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.UUID;

@Repository
public interface CronogramaRepository extends JpaRepository<Cronograma, Long> {
    Page<Cronograma> findAllByAtivoTrue(Pageable pageable);
    Page<Cronograma> findByAtivoTrueAndTipoAtendimento_Id(Long id, Pageable pageable);

    Page<Cronograma> findAllByAtivoTrueAndProfissional_UserId(UUID profissionalUserId, Pageable pageable);
    boolean existsByTipoAtendimento_Id(Long tipoAtendimentoId);
}
