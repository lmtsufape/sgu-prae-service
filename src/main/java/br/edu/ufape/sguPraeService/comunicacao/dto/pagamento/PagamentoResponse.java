package br.edu.ufape.sguPraeService.comunicacao.dto.pagamento;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.modelmapper.ModelMapper;

import br.edu.ufape.sguPraeService.comunicacao.dto.beneficio.BeneficioResponse;
import br.edu.ufape.sguPraeService.models.Pagamento;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class PagamentoResponse {

    private Long id;
    private BigDecimal valor;
	private LocalDate data;
    private Integer mesReferencia;
    private Integer anoReferencia;
    private Long numeroLote;
    private BeneficioResponse beneficio;

    public PagamentoResponse(Pagamento pagamento, ModelMapper modelMapper){
        if (pagamento == null) throw new IllegalArgumentException("Pagamento não pode ser nulo");
        pagamento.getBeneficio().getPagamentos().clear();
        modelMapper.map(pagamento, this);
    }
}
