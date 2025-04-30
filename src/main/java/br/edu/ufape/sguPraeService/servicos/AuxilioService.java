package br.edu.ufape.sguPraeService.servicos;

import br.edu.ufape.sguPraeService.dados.AuxilioRepository;
import br.edu.ufape.sguPraeService.exceptions.AuxilioNotFoundException;
import br.edu.ufape.sguPraeService.models.Auxilio;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuxilioService implements br.edu.ufape.sguPraeService.servicos.interfaces.AuxilioService {
	private final AuxilioRepository auxilioRepository;
	private final ModelMapper modelMapper;

	@Override
	public List<Auxilio> listar() {
		return auxilioRepository.findAll();
	}

	@Override
	public Auxilio buscar(Long id) throws AuxilioNotFoundException {
		return auxilioRepository.findById(id).orElseThrow(AuxilioNotFoundException::new);
	}

	@Override
	public Auxilio salvar(Auxilio entity) {
		return auxilioRepository.save(entity);
	}

	@Override
	public Auxilio editar(Long id, Auxilio entity) throws AuxilioNotFoundException {
		Auxilio auxilio = buscar(id);
		modelMapper.map(entity, auxilio);
		return auxilioRepository.save(auxilio);
	}

	@Override
	public void deletar(Long id) throws AuxilioNotFoundException {
		Auxilio auxilio = buscar(id);
		auxilio.setAtivo(false);
		auxilioRepository.save(auxilio);
	}

	@Override
    public BigDecimal calcularValorProporcional(Auxilio auxilio, LocalDate inicio, LocalDate fim) {
        LocalDate dataInicio = auxilio.getInicioBolsa().isBefore(inicio) ? inicio : auxilio.getInicioBolsa();
        LocalDate dataFim = auxilio.getFimBolsa().isAfter(fim) ? fim : auxilio.getFimBolsa();

        if (dataInicio.isAfter(dataFim)) {
            return BigDecimal.ZERO;
        }

        long diasNoPeriodo = ChronoUnit.DAYS.between(dataInicio, dataFim.plusDays(1));
        long diasTotais = ChronoUnit.DAYS.between(auxilio.getInicioBolsa(), auxilio.getFimBolsa().plusDays(1));

        return auxilio.getValorBolsa()
                .multiply(BigDecimal.valueOf(diasNoPeriodo))
                .divide(BigDecimal.valueOf(diasTotais), RoundingMode.HALF_UP);
    }
}
