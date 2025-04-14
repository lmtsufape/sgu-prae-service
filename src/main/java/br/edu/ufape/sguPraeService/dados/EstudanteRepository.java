package br.edu.ufape.sguPraeService.dados;

import br.edu.ufape.sguPraeService.models.Estudante;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EstudanteRepository extends JpaRepository<Estudante, Long> {
    Optional<Object> findByUserId(UUID userId);

    List<Estudante> findAllByAtivoTrue();
}
