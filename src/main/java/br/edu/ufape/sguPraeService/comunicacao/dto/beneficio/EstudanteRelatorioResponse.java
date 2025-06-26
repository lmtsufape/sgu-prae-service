package br.edu.ufape.sguPraeService.comunicacao.dto.beneficio;

import br.edu.ufape.sguPraeService.comunicacao.dto.usuario.AlunoResponse;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EstudanteRelatorioResponse {
    private String nome;
    private String cpf;
    private String matricula;
    private String email;
    private String telefone;
    private AlunoResponse.Curso curso;
}