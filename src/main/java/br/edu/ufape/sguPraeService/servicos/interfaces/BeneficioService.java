package br.edu.ufape.sguPraeService.servicos.interfaces;

import br.edu.ufape.sguPraeService.exceptions.BeneficioNotFoundException;
import br.edu.ufape.sguPraeService.models.Beneficio;
import br.edu.ufape.sguPraeService.models.Estudante;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BeneficioService {
    Page<Beneficio> listar(Pageable pageable);

    List<Beneficio> listar();

    Page<Beneficio> listarPorEstudante(Long estudanteId, Pageable pageable);

    List<Beneficio> listarPorEstudante(Long estudanteId);

    Beneficio buscar(Long id) throws BeneficioNotFoundException;

    Beneficio salvar(Beneficio entity);

    Beneficio editar(Beneficio old, Beneficio entity) throws BeneficioNotFoundException;

    void deletar(Long id) throws BeneficioNotFoundException;

    Page<Beneficio> listarPorTipo(Long tipoId, Pageable pageable) throws BeneficioNotFoundException;

    List<Beneficio> buscarPorPagamento(Long pagamentoId);

    List<Beneficio> listarPagosPorMes() throws BeneficioNotFoundException;

    List<Beneficio> listarBeneficiosPendentesMesAtual();

    Page<Estudante> listarEstudantesComBeneficioAtivo(Pageable pageable);

    List<Estudante> listarEstudantesPorAuxilio(Long id);

    Page<Estudante> listarEstudantesPorAuxilio(Long id, Pageable pageable);
}
