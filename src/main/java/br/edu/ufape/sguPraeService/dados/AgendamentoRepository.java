package br.edu.ufape.sguPraeService.dados;

import br.edu.ufape.sguPraeService.models.Agendamento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.UUID;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {


    Page<Agendamento> findAllByEstudante_UserIdAndAtivoTrue(UUID userId, Pageable pageable);

    @Query("SELECT a FROM Agendamento a " +
            "JOIN a.vaga v " +
            "JOIN v.cronograma c " +
            "JOIN c.profissional p " +
            "WHERE p.userId = :userId and  a.ativo = true")
    Page<Agendamento> findAllByProfissionalUserId(@Param("userId") UUID userId, Pageable pageable);


}