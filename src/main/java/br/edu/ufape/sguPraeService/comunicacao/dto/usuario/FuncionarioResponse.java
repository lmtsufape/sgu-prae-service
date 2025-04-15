package br.edu.ufape.sguPraeService.comunicacao.dto.usuario;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FuncionarioResponse {
    UUID id;
    String nome;
    String nomeSocial;
    String cpf;
    String email;
    String telefone;
    String siape;
}
