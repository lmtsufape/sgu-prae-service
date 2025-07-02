package br.edu.ufape.sguPraeService.comunicacao.dto.beneficio;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.modelmapper.ModelMapper;

import br.edu.ufape.sguPraeService.comunicacao.dto.tipoBeneficio.TipoBeneficioResponse;
import br.edu.ufape.sguPraeService.models.Beneficio;
import br.edu.ufape.sguPraeService.models.Estudante;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BeneficioResponse {

	Long id;

	private TipoBeneficioResponse tipoBeneficio;

	private int horasBeneficio;
	private LocalDate inicioBeneficio;
	private LocalDate fimBeneficio;
	private String parecerTermino;
	private BigDecimal valorPagamento;
	private boolean status;
	private Estudante estudantes;

	public BeneficioResponse(Beneficio beneficio, ModelMapper modelMapper) {
		if (beneficio == null)
			throw new IllegalArgumentException("O auxílio não pode ser nulo");
		modelMapper.map(beneficio, this);
		if (beneficio.getTipoBeneficio() != null)
			tipoBeneficio = new TipoBeneficioResponse(beneficio.getTipoBeneficio(), modelMapper);


	}
}