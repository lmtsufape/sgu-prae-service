package br.edu.ufape.sguPraeService.servicos;

import java.time.LocalDate;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import br.edu.ufape.sguPraeService.dados.AuxilioRepository;
import br.edu.ufape.sguPraeService.exceptions.AuxilioNotFoundException;
import br.edu.ufape.sguPraeService.models.Auxilio;
import lombok.RequiredArgsConstructor;

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
		if (entity.getTipoAuxilio() != null) {
            auxilio.setTipoBolsa(null);
        }

        if (entity.getTipoBolsa() != null) {
            auxilio.setTipoAuxilio(null);
        }
		return auxilioRepository.save(auxilio);
	}

	@Override
	public void deletar(Long id) throws AuxilioNotFoundException {
		Auxilio auxilio = buscar(id);
		auxilio.setAtivo(false);
		auxilioRepository.save(auxilio);
	}

    @Override
    public List<Auxilio> listarPagosPorMes() throws AuxilioNotFoundException {
        return auxilioRepository.findByAtivoTrue();
    }

    @Override
    public List<Auxilio> listarPorTipo(Long tipoId) throws AuxilioNotFoundException {
        return auxilioRepository.findByTipoAuxilioId(tipoId);
    }

	public List<Auxilio> listarAuxiliosPendentesMesAtual() {
		List<Auxilio> ativos = auxilioRepository.findByAtivoTrue();
		LocalDate agora = LocalDate.now();

		return ativos.stream()
				.filter(aux -> aux.getPagamentos().stream()
						.noneMatch(p -> p.getData() != null &&
								p.getData().getMonth() == agora.getMonth() &&
								p.getData().getYear() == agora.getYear()))
				.toList();
	}
}
