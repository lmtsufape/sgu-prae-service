package br.edu.ufape.sguPraeService.dados;

import br.edu.ufape.sguPraeService.models.CancelamentoAgendamento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface CancelamentoAgendamentoRepository extends JpaRepository<CancelamentoAgendamento, Long> {
  // ATUALIZADO: userId -> Id
  Page<CancelamentoAgendamento> findAllByAgendamento_Estudante_Id(UUID estudanteId, Pageable pageable);

  @Query("SELECT a FROM CancelamentoAgendamento a " +
          "JOIN a.agendamento ag " +
          "JOIN ag.vaga v " +
          "JOIN v.cronograma c " +
          "JOIN c.profissional p " +
          "WHERE p.id = :id")
  Page<CancelamentoAgendamento> findAllByProfissionalId(@Param("id") UUID id, Pageable pageable);
}