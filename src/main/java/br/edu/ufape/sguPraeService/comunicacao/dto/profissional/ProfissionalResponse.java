package br.edu.ufape.sguPraeService.comunicacao.dto.profissional;

import br.edu.ufape.sguPraeService.comunicacao.dto.usuario.UsuarioResponse;
import br.edu.ufape.sguPraeService.models.Profissional;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter @Setter @NoArgsConstructor
public class ProfissionalResponse extends UsuarioResponse {

    // Herda ID (UUID), nome, cpf, email, etc.
    private String siape;
    private String especialidade;

    public ProfissionalResponse(Profissional profissional, ModelMapper modelMapper){
        super(profissional, modelMapper); // Mapeia os dados do pai (Usuario)
        this.siape = profissional.getSiape();
        this.especialidade = profissional.getEspecialidade();
    }
}