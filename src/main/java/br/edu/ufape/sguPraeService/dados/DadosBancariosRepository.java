package br.edu.ufape.sguPraeService.dados;

import br.edu.ufape.sguPraeService.models.DadosBancarios;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DadosBancariosRepository extends JpaRepository<DadosBancarios, Long> {
}
