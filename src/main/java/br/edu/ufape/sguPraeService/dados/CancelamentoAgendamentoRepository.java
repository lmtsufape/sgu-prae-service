package br.edu.ufape.sguPraeService.dados;

import br.edu.ufape.sguPraeService.models.CancelamentoAgendamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CancelamentoAgendamentoRepository extends JpaRepository<CancelamentoAgendamento, Long> {
  List<CancelamentoAgendamento> findAllByAgendamento_Estudante_UserId(String agendamentoEstudanteUserId);

  @Query("SELECT a FROM CancelamentoAgendamento a " +
          "JOIN a.agendamento ag " +
          "JOIN ag.vaga v " +
          "JOIN v.cronograma c " +
          "JOIN c.profissional p " +
          "WHERE p.userId = :userId")
  List<CancelamentoAgendamento> findAllByProfissionalUserId(@Param("userId") String userId);
}