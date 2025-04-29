package br.edu.ufape.sguPraeService.comunicacao.dto.pagamento;

import br.edu.ufape.sguPraeService.models.Pagamento;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.modelmapper.ModelMapper;

@Getter @Setter @NoArgsConstructor
public class PagamentoResponse {

    Long id;
    private BigDecimal valor;
	private LocalDate data;

    public PagamentoResponse(Pagamento pagamento, ModelMapper modelMapper){
        if (pagamento == null) throw new IllegalArgumentException("Pagamento não pode ser nulo");
        else modelMapper.map(pagamento, this);
    }
}
