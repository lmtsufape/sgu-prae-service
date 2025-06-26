package br.edu.ufape.sguPraeService.dados;

import br.edu.ufape.sguPraeService.models.Estudante;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EstudanteRepository extends JpaRepository<Estudante, Long> {
    Optional<Object> findByUserId(UUID userId);

    Page<Estudante> findByUserIdIn(List<UUID> userIds, Pageable pageable);

    Page<Estudante> findAllByAtivoTrue(Pageable pageable);


}
