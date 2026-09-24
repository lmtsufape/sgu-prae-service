package br.edu.ufape.sguPraeService.comunicacao.dto.profissional;

import br.edu.ufape.sguPraeService.comunicacao.dto.usuario.UsuarioRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class ProfissionalRequest extends UsuarioRequest {

    @NotBlank(message = "O SIAPE é obrigatório")
    private String siape;

    @NotBlank(message = "Especialidade é obrigatória")
    @Size(min = 3, max = 100, message = "Especialidade deve ter entre 3 e 100 caracteres")
    private String especialidade;
}