package br.edu.ufape.sguPraeService.dados;

import br.edu.ufape.sguPraeService.models.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnderecoRepository extends JpaRepository<Endereco, Long> {
}
