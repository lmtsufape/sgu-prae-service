package br.edu.ufape.sguPraeService.dados;

import br.edu.ufape.sguPraeService.models.Profissional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfissionalRepository extends JpaRepository<Profissional, Long> {
    Optional<Profissional> findByUserId(String userId);
}
