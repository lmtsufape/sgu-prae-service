package br.edu.ufape.sguPraeService.comunicacao.dto.auxilio;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.modelmapper.ModelMapper;

import br.edu.ufape.sguPraeService.comunicacao.dto.estudante.EstudanteResponse;
import br.edu.ufape.sguPraeService.comunicacao.dto.pagamento.PagamentoResponse;
import br.edu.ufape.sguPraeService.comunicacao.dto.tipoAuxilio.TipoAuxilioResponse;
import br.edu.ufape.sguPraeService.comunicacao.dto.tipoBolsa.TipoBolsaResponse;
import br.edu.ufape.sguPraeService.models.Auxilio;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AuxilioResponse {

	Long id;

	private TipoAuxilioResponse tipoAuxilio;

	private TipoBolsaResponse tipoBolsa;

	private int horasBolsa;
	private LocalDate inicioBolsa;
	private LocalDate fimBolsa;
	private String parecerTermino;
	private BigDecimal valorBolsa;
	private boolean status;
	private List<EstudanteResponse> estudantes;
	private List<PagamentoResponse> pagamentos;

	public AuxilioResponse(Auxilio auxilio, ModelMapper modelMapper) {
		if (auxilio == null)
			throw new IllegalArgumentException("O auxílio não pode ser nulo");
		auxilio.getPagamentos().forEach(x -> x.setAuxilio(null));
		auxilio.getEstudantes().forEach(x->x.setAuxilios(null));;
		modelMapper.map(auxilio, this);
		if (auxilio.getTipoAuxilio() != null)
			tipoAuxilio = new TipoAuxilioResponse(auxilio.getTipoAuxilio(), modelMapper);
		if (auxilio.getTipoBolsa() != null)
			tipoBolsa = new TipoBolsaResponse(auxilio.getTipoBolsa(), modelMapper);

	}
}