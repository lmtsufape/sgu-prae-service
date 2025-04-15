package br.edu.ufape.sguPraeService.comunicacao.dto.estudante;

import br.edu.ufape.sguPraeService.models.DadosBancarios;
import br.edu.ufape.sguPraeService.models.Auxilio;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CredorResponse {
    private EstudanteResponse estudante;
    private DadosBancarios dadosBancarios;
    private Auxilio auxilio;

}