package br.edu.ufape.sguPraeService.comunicacao.dto.gestor;

import br.edu.ufape.sguPraeService.comunicacao.dto.usuario.UsuarioResponse;
import br.edu.ufape.sguPraeService.models.Gestor;
import br.edu.ufape.sguPraeService.models.Usuario;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class GestorResponse extends UsuarioResponse {
    String siape;

    public GestorResponse(Gestor gestor, ModelMapper modelMapper){
        super(gestor, modelMapper); // Chama o construtor de UsuarioResponse (mapeia id, nome, cpf, etc)
        this.siape = gestor.getSiape();
    }
}
