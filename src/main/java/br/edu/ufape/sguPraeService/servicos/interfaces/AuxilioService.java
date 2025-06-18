package br.edu.ufape.sguPraeService.servicos.interfaces;

import br.edu.ufape.sguPraeService.exceptions.AuxilioNotFoundException;
import br.edu.ufape.sguPraeService.models.Auxilio;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AuxilioService {
    List<Auxilio> listar();

    Page<Auxilio> listar(Pageable pageable);

    Auxilio buscar(Long id) throws AuxilioNotFoundException;

    Auxilio salvar(Auxilio entity);

    Auxilio editar(Long id, Auxilio entity) throws AuxilioNotFoundException;

    void deletar(Long id) throws AuxilioNotFoundException;

    Auxilio buscarPorPagamentoId(Long pagamentoId) throws AuxilioNotFoundException;

    List<Auxilio> buscarPorEstudanteId(Long estudanteId);

    Page<Auxilio> buscarPorEstudanteId(Long estudanteId, Pageable pageable);

    List<Auxilio> listarPorTipo(Long tipoId) throws AuxilioNotFoundException;

    Page<Auxilio> listarPorTipo(Long tipoId, Pageable pageable) throws AuxilioNotFoundException;

    List<Auxilio> listarPagosPorMes(int ano, int mes) throws AuxilioNotFoundException;

    Page<Auxilio> listarPagosPorMes(int ano, int mes, Pageable pageable) throws AuxilioNotFoundException;

    List<Auxilio> listarAuxiliosPendentesMesAtual();

    Page<Auxilio> listarAuxiliosPendentesMesAtual(Pageable pageable);
}
