package br.edu.ufape.sguPraeService.comunicacao.dto.gestor;

import br.edu.ufape.sguPraeService.comunicacao.dto.usuario.UsuarioRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class GestorRequest extends UsuarioRequest {
    @NotBlank(message = "O SIAPE é obrigatório")
    private String siape;
}