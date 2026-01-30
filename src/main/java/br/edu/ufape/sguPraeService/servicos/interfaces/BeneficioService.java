package br.edu.ufape.sguPraeService.servicos.interfaces;

import br.edu.ufape.sguPraeService.exceptions.BeneficioNotFoundException;
import br.edu.ufape.sguPraeService.models.Beneficio;
import br.edu.ufape.sguPraeService.models.Estudante;
import br.edu.ufape.sguPraeService.models.enums.MotivoEncerramento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.querydsl.core.types.Predicate;

import java.util.List;

public interface BeneficioService {
    Page<Beneficio> listar(Predicate predicate, Pageable pageable);

    List<Beneficio> listar();

    Page<Beneficio> listarPorEstudante(Long estudanteId, Pageable pageable);

    List<Beneficio> listarPorEstudante(Long estudanteId);

    Beneficio buscar(Long id) throws BeneficioNotFoundException;

    Beneficio salvar(Beneficio entity);

    Beneficio editar(Beneficio old, Beneficio entity) throws BeneficioNotFoundException;

    void deletar(Long id) throws BeneficioNotFoundException;

    Page<Beneficio> listarPorTipo(Long tipoId, Pageable pageable) throws BeneficioNotFoundException;

    List<Beneficio> buscarPorPagamento(Long pagamentoId);

    Page<Beneficio> listarPagosPorMes(Pageable pageable);

    Page<Beneficio> listarBeneficiosPendentesMesAtual(Predicate predicate, Pageable pageable);

    Page<Estudante> listarEstudantesComBeneficioAtivo(Pageable pageable);

    List<Estudante> listarEstudantesPorAuxilio(Long id);

    Page<Estudante> listarEstudantesPorAuxilio(Long id, Pageable pageable);

    Long contarEstudantesBeneficiados();

    Long contarCursosDistintosComBeneficioAtivo();

    List<java.util.Map<String, Object>> obterQuantidadeBeneficiadosPorCurso();
}
