package br.edu.ufape.sguPraeService.servicos.interfaces;

import br.edu.ufape.sguPraeService.exceptions.notFoundExceptions.EstudanteNotFoundException;
import br.edu.ufape.sguPraeService.models.Estudante;
import jakarta.transaction.Transactional;

import java.util.List;

public interface EstudanteService {
    @Transactional
    Estudante salvarEstudante(Estudante estudante);

    Estudante buscarEstudante(Long id) throws EstudanteNotFoundException;

    Estudante buscarPorUserId(String userId) throws EstudanteNotFoundException;

    List<Estudante> listarEstudantes();

    Estudante atualizarEstudante(Estudante estudante, Estudante existente) throws EstudanteNotFoundException;

    void deletarEstudante(Long id) throws EstudanteNotFoundException;

    List<Estudante> listarEstudantesComAuxilioAtivo();

    List<Estudante> listarEstudantesPorAuxilioId(Long auxilioId);
}