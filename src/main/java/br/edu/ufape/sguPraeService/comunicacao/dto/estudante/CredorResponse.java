package br.edu.ufape.sguPraeService.comunicacao.dto.estudante;

import br.edu.ufape.sguPraeService.comunicacao.dto.usuario.AlunoResponse;
import br.edu.ufape.sguPraeService.models.DadosBancarios;
import br.edu.ufape.sguPraeService.models.Auxilio;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CredorResponse {
    private String nomeEstudante;
    private DadosBancarios dadosBancarios;
    private Long idAuxilio;

    public CredorResponse(AlunoResponse aluno, DadosBancarios dadosBancarios, Auxilio auxilio) {
        if (aluno == null) throw new IllegalArgumentException("Aluno não pode ser nulo");
        if (dadosBancarios == null) throw new IllegalArgumentException("Dados bancários não podem ser nulos");
        if (auxilio == null) throw new IllegalArgumentException("Auxílio não pode ser nulo");

        this.nomeEstudante = aluno.getNome();
        this.dadosBancarios = dadosBancarios;
        this.idAuxilio = auxilio.getId();
    }
}