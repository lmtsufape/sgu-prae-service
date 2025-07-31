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
public class AlunoResponse {
    UUID id;
    String nome;
    String nomeSocial;
    String cpf;
    String email;
    String telefone;
    String matricula;
    private Curso curso;
    private TipoEtnia tipoEtnia;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Curso {
        private Long id;
        private String nome;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TipoEtnia {
        private Long id;
        private String tipo;
    }

}
