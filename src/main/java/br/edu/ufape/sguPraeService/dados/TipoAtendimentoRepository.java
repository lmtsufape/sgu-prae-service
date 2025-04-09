package br.edu.ufape.sguPraeService.dados;

import br.edu.ufape.sguPraeService.models.TipoAtendimento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoAtendimentoRepository extends JpaRepository<TipoAtendimento, Long> {
}
