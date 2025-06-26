package br.edu.ufape.sguPraeService.servicos;

import java.time.LocalDate;
import java.util.List;

import br.edu.ufape.sguPraeService.models.Beneficio;
import br.edu.ufape.sguPraeService.models.Estudante;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.edu.ufape.sguPraeService.dados.BeneficioRepository;
import br.edu.ufape.sguPraeService.exceptions.BeneficioNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BeneficioService implements br.edu.ufape.sguPraeService.servicos.interfaces.BeneficioService {
	private final BeneficioRepository beneficioRepository;
	private final ModelMapper modelMapper;

	@Override
	public Page<Beneficio> listar(Pageable pageable) {
		return beneficioRepository.findAll(pageable);
	}

	@Override
	public List<Beneficio> listar() {
		return beneficioRepository.findAll();
	}

	@Override
	public List<Beneficio> listarPorEstudante(Long estudanteId) {
		return beneficioRepository.findAllByAtivoTrueAndEstudantes_Id(estudanteId);
	}

	@Override
	public Page<Beneficio> listarPorEstudante(Long estudanteId, Pageable pageable) {
		return beneficioRepository.findAllByAtivoTrueAndEstudantes_Id(estudanteId, pageable);
	}

	@Override
	public Beneficio buscar(Long id) throws BeneficioNotFoundException {
		return beneficioRepository.findById(id).orElseThrow(BeneficioNotFoundException::new);
	}

	@Override
	public Beneficio salvar(Beneficio entity) {
		return beneficioRepository.save(entity);
	}

	@Override
	public Beneficio editar(Beneficio old, Beneficio entity) throws BeneficioNotFoundException {
		modelMapper.map(entity, old);
		return beneficioRepository.save(old);
	}

	@Override
	public void deletar(Long id) throws BeneficioNotFoundException {
		Beneficio beneficio = buscar(id);
		beneficio.setAtivo(false);
		beneficioRepository.save(beneficio);
	}

	@Override
	public List<Beneficio> buscarPorPagamento(Long pagamentoId) {
		return beneficioRepository.findByPagamentos_Id(pagamentoId);
	}

    @Override
    public List<Beneficio> listarPagosPorMes()  {
        return beneficioRepository.findByAtivoTrue();
    }

    @Override
    public Page<Beneficio> listarPorTipo(Long tipoId, Pageable pageable) {
        return beneficioRepository.findByTipoBeneficioId(tipoId, pageable);
    }

	@Override
	public List<Beneficio> listarBeneficiosPendentesMesAtual() {
		List<Beneficio> ativos = beneficioRepository.findByAtivoTrue();
		LocalDate agora = LocalDate.now();

		return ativos.stream()
				.filter(aux -> aux.getPagamentos().stream()
						.noneMatch(p -> p.getData() != null &&
								p.getData().getMonth() == agora.getMonth() &&
								p.getData().getYear() == agora.getYear()))
				.toList();
	}

	@Override
	public Page<Estudante> listarEstudantesComBeneficioAtivo(Pageable pageable) {
		return beneficioRepository.findAllEstudantesByAtivoTrue(pageable);
	}

	@Override
	public List<Estudante> listarEstudantesPorAuxilio(Long id) {
		return beneficioRepository.findEstudantesByBeneficioId(id);
	}

	@Override
	public Page<Estudante> listarEstudantesPorAuxilio(Long id, Pageable pageable) {
		return beneficioRepository.findEstudantesByBeneficioId(id, pageable);
	}
}
