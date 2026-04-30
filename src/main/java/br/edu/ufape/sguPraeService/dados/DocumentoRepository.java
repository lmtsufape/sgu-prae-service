package br.edu.ufape.sguPraeService.dados;

import br.edu.ufape.sguPraeService.models.Documento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DocumentoRepository extends JpaRepository<Documento, Long> {

    List<Documento> findByUserId(UUID userId);
    Optional<Documento> findByIdAndUserId(Long id, UUID userId);

    @Query("SELECT CASE WHEN COUNT(d) > 0 THEN true ELSE false END FROM Documento d WHERE d.path LIKE %:filename% AND (" +
            "(EXISTS (SELECT 1 FROM Beneficio b WHERE b.termo = d AND b.ativo = true)) OR " +
            "(EXISTS (SELECT 1 FROM Estudante e JOIN e.documentos ed WHERE ed = d AND e.ativo = true)))")
    boolean isArquivoEssencialParaOSistema(@Param("filename") String filename);
}