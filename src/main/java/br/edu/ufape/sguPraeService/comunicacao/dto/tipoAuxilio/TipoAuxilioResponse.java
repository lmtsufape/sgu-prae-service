package br.edu.ufape.sguPraeService.comunicacao.dto.tipoAuxilio;
import br.edu.ufape.sguPraeService.models.TipoAuxilio;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import java.math.BigDecimal;

@Getter @Setter @NoArgsConstructor
public class TipoAuxilioResponse {

    Long id;
    String tipo;
    BigDecimal valorAuxilio;

    public TipoAuxilioResponse(TipoAuxilio tipoAuxilio, ModelMapper modelMapper){
        if (tipoAuxilio == null) throw new IllegalArgumentException("Tipo Auxílio não pode ser nulo");
        else modelMapper.map(tipoAuxilio, this);
    }
}