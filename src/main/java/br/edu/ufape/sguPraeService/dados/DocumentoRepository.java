package br.edu.ufape.sguPraeService.dados;

import br.edu.ufape.sguPraeService.models.Documento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DocumentoRepository extends JpaRepository<Documento, Long> {
    Optional<Documento> findByNome(String nome);
}
