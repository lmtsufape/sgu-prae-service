package br.edu.ufape.sguPraeService.dados;

import br.edu.ufape.sguPraeService.models.Documento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DocumentoRepository extends JpaRepository<Documento, Long> {
    List<Documento> findByUserId(UUID userId);
    Optional<Documento> findByIdAndUserId(Long id, UUID userId);
}
