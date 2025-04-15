package br.edu.ufape.sguPraeService.comunicacao.dto.auxilio;
import br.edu.ufape.sguPraeService.models.Auxilio;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter @Setter
public class AuxilioResponse {
    Long id;
    int horasBolsa;
    LocalDate inicioBolsa;
    LocalDate fimBolsa;
    String parecerTermino;
    BigDecimal valorBolsa;

    public AuxilioResponse(Auxilio auxilio, ModelMapper modelMapper){
        if (auxilio == null) throw new IllegalArgumentException("O auxílio não pode ser nulo");
        else modelMapper.map(auxilio, this);
    }
}