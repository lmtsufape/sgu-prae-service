package br.edu.ufape.sguPraeService.servicos;

import java.time.LocalDate;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
	public Page<Auxilio> listar(Pageable pageable) {
		return auxilioRepository.findAll(pageable);
	}

	@Override
	public Auxilio buscar(Long id) throws AuxilioNotFoundException {
		return auxilioRepository.findById(id).orElseThrow(AuxilioNotFoundException::new);
	}

	@Override
	public Auxilio buscarPorPagamentoId(Long pagamentoId) throws AuxilioNotFoundException {
		return auxilioRepository.findByPagamentos_Id(pagamentoId).orElseThrow(AuxilioNotFoundException::new);
	}

	@Override
	public List<Auxilio> buscarPorEstudanteId(Long estudanteId) {
		return auxilioRepository.findByEstudantes_Id(estudanteId);
	}

	@Override
	public Page<Auxilio> buscarPorEstudanteId(Long estudanteId, Pageable pageable) {
		return auxilioRepository.findByEstudantes_Id(estudanteId, pageable);
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
    public List<Auxilio> listarPagosPorMes(int ano, int mes) throws AuxilioNotFoundException {
        return auxilioRepository.listarAuxiliosPagosMes(mes, ano);
    }

	@Override
    public Page<Auxilio> listarPagosPorMes(int ano, int mes, Pageable pageable) throws AuxilioNotFoundException {
        return auxilioRepository.listarAuxiliosPagosMes(mes, ano, pageable);
    }

    @Override
    public List<Auxilio> listarPorTipo(Long tipoId) throws AuxilioNotFoundException {
        return auxilioRepository.findByTipoAuxilioId(tipoId);
    }

	@Override
    public Page<Auxilio> listarPorTipo(Long tipoId, Pageable pageable) throws AuxilioNotFoundException {
        return auxilioRepository.findByTipoAuxilioId(tipoId, pageable);
    }

	@Override
	public List<Auxilio> listarAuxiliosPendentesMesAtual() {
		LocalDate agora = LocalDate.now();
    	return auxilioRepository.listarAuxiliosPendentesMesAtual(agora.getMonthValue(), agora.getYear());
	}

	@Override
	public Page<Auxilio> listarAuxiliosPendentesMesAtual(Pageable pageable) {
		LocalDate agora = LocalDate.now();
    	return auxilioRepository.listarAuxiliosPendentesMesAtual(agora.getMonthValue(), agora.getYear(), pageable);
	}
	
}
