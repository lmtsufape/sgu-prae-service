package br.edu.ufape.sguPraeService.dados;

import br.edu.ufape.sguPraeService.models.Estudante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EstudanteRepository extends JpaRepository<Estudante, Long> {
    Optional<Object> findByUserId(UUID userId);

    List<Estudante> findAllByAtivoTrue();

    @Query("SELECT DISTINCT e FROM Estudante e JOIN FETCH e.auxilios a WHERE a.ativo = true")
    List<Estudante> findAllWithAuxilioAtivo();

     @Query("SELECT e FROM Estudante e JOIN e.auxilios a WHERE a.id = :auxilioId")
    List<Estudante> findByAuxilioId(Long auxilioId);

}
