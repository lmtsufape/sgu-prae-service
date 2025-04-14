package br.edu.ufape.sguPraeService.servicos.interfaces;

import br.edu.ufape.sguPraeService.exceptions.notFoundExceptions.ProfissionalNotFoundException;
import br.edu.ufape.sguPraeService.models.Profissional;

import java.util.List;
import java.util.UUID;

public interface ProfissionalService {
    List<Profissional> listar();

    Profissional buscar(Long id) throws ProfissionalNotFoundException;

    Profissional buscarPorUserId(UUID id) throws ProfissionalNotFoundException;

    Profissional salvar(Profissional entity);

    Profissional editar(UUID userId, Profissional entity) throws ProfissionalNotFoundException;

    void deletar(Long id) throws ProfissionalNotFoundException;
}
