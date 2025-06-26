package br.edu.ufape.sguPraeService.comunicacao.dto.tipoBeneficio;
import br.edu.ufape.sguPraeService.models.TipoBeneficio;
import br.edu.ufape.sguPraeService.models.enums.NaturezaBeneficio;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import java.math.BigDecimal;

@Getter @Setter @NoArgsConstructor
public class TipoBeneficioResponse {

    Long id;
    String tipo;
    NaturezaBeneficio naturezaBeneficio;
    String descricao;
    private BigDecimal valorBeneficio;


    public TipoBeneficioResponse(TipoBeneficio tipoBeneficio, ModelMapper modelMapper){
        if (tipoBeneficio == null) throw new IllegalArgumentException("Tipo Auxílio não pode ser nulo");
        else modelMapper.map(tipoBeneficio, this);
    }
}