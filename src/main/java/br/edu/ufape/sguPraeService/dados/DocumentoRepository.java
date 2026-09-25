package br.edu.ufape.sguPraeService.dados;

import br.edu.ufape.sguPraeService.models.Documento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface DocumentoRepository extends JpaRepository<Documento, Long> {


    @Query("SELECT CASE WHEN COUNT(d) > 0 THEN true ELSE false END FROM Documento d " +
            "WHERE d.path LIKE %:filename% AND (" +
            "d IN (SELECT b.termo FROM Beneficio b WHERE b.ativo = true) OR " +
            "d IN (SELECT ed FROM Estudante e JOIN e.documentos ed WHERE e.ativo = true))")
    boolean isArquivoEssencialParaOSistema(@Param("filename") String filename);
}