package br.edu.ufape.sguPraeService.dados;

import br.edu.ufape.sguPraeService.models.Agendamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {


    List<Agendamento> findAllByEstudante_UserIdAndAtivoTrue(String userId);

    @Query("SELECT a FROM Agendamento a " +
            "JOIN a.vaga v " +
            "JOIN v.cronograma c " +
            "JOIN c.profissional p " +
            "WHERE p.userId = :userId and  a.ativo = true")
    List<Agendamento> findAllByProfissionalUserId(@Param("userId") String userId);


}