package br.edu.ufape.sguPraeService.dados;

import br.edu.ufape.sguPraeService.models.Beneficio;
import br.edu.ufape.sguPraeService.models.Estudante;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;

public interface BeneficioRepository extends JpaRepository<Beneficio, Long> {
  List<Beneficio> findByAtivoTrue();

  @Query("SELECT b FROM Beneficio b WHERE b.tipoBeneficio.id = :tipoId")
  Page<Beneficio> findByTipoBeneficioId(@Param("tipoId") Long tipoId, Pageable pageable);

  List<Beneficio> findByPagamentos_Id(Long pagamentoId);

  List<Beneficio> findAllByAtivoTrueAndEstudantes_Id(Long estudanteId);

  Page<Beneficio> findAllByAtivoTrueAndEstudantes_Id(Long estudanteId, Pageable pageable);

  @Query("SELECT b.estudantes FROM Beneficio b WHERE b.ativo = true")
  Page<Estudante> findAllEstudantesByAtivoTrue(Pageable pageable);

  @Query("SELECT b.estudantes FROM Beneficio b WHERE b.ativo = true AND b.id = :beneficioId")
    List<Estudante> findEstudantesByBeneficioId(@Param("beneficioId") Long beneficioId);

  @Query("SELECT b.estudantes FROM Beneficio b WHERE b.ativo = true AND b.id = :beneficioId")
  Page<Estudante> findEstudantesByBeneficioId(@Param("beneficioId") Long beneficioId, Pageable pageable);
}