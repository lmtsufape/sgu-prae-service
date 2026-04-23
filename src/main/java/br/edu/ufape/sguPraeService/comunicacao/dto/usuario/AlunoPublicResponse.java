package br.edu.ufape.sguPraeService.comunicacao.dto.usuario;

import lombok.Data;
import java.util.UUID;

@Data
public class AlunoPublicResponse {
    private UUID id;
    private CursoPublicDTO curso;

    @Data
    public static class CursoPublicDTO {
        private Long id;
        private String nome;
    }
}