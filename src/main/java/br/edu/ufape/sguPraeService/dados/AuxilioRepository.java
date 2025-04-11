package br.edu.ufape.sguPraeService.dados;

import br.edu.ufape.sguPraeService.models.Auxilio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuxilioRepository extends JpaRepository<Auxilio, Long> {
  }