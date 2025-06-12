package br.edu.ufape.sguPraeService.dados;

import java.util.Optional;
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
}