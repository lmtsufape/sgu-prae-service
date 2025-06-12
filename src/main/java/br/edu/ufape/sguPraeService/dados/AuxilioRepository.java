package br.edu.ufape.sguPraeService.dados;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import br.edu.ufape.sguPraeService.models.Auxilio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AuxilioRepository extends JpaRepository<Auxilio, Long> {
  List<Auxilio> findByAtivoTrue();

  @Query("SELECT a FROM Auxilio a WHERE a.tipoAuxilio.id = :tipoId")
  List<Auxilio> findByTipoAuxilioId(@Param("tipoId") Long tipoId);

  Optional<Auxilio> findByPagamentos_Id(Long pagamentoId);

  List<Auxilio> findByEstudantes_Id(Long estudanteId);

  Page<Auxilio> findByAtivoTrue(Pageable pageable);

  Page<Auxilio> findByTipoAuxilioId(Long tipoId, Pageable pageable);

  Page<Auxilio> findByEstudantes_Id(Long estudanteId, Pageable pageable);

  @Query("""
      SELECT a FROM Auxilio a
      WHERE a.ativo = true
      AND NOT EXISTS (
          SELECT p FROM Pagamento p
          WHERE p MEMBER OF a.pagamentos
          AND p.data IS NOT NULL
          AND DATE_PART('month', p.data) = :mes
          AND DATE_PART('year', p.data) = :ano
      )
      """)
  Page<Auxilio> listarAuxiliosPendentesMesAtual(@Param("mes") int mes, @Param("ano") int ano, Pageable pageable);

}