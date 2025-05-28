package br.edu.ufape.sguPraeService.comunicacao.dto.pagamento;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.modelmapper.ModelMapper;

import br.edu.ufape.sguPraeService.comunicacao.dto.auxilio.AuxilioResponse;
import br.edu.ufape.sguPraeService.models.Pagamento;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class PagamentoResponse {

    private Long id;
    private BigDecimal valor;
	private LocalDate data;
    private AuxilioResponse auxilio;

    public PagamentoResponse(Pagamento pagamento, ModelMapper modelMapper){
        if (pagamento == null) throw new IllegalArgumentException("Pagamento não pode ser nulo");
        pagamento.getAuxilio().getPagamentos().clear();
        pagamento.getAuxilio().getEstudantes().forEach(x->x.getAuxilios().clear());
        modelMapper.map(pagamento, this);
    }
}
