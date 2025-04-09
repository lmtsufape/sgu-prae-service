package br.edu.ufape.sguPraeService.comunicacao.dto.usuario;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FuncionarioResponse {
    String nome;
    String nomeSocial;
    String cpf;
    String email;
    String telefone;
    String siape;
}
