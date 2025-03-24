package br.edu.ufape.sguPraeService.servicos.interfaces;

import br.edu.ufape.sguPraeService.exceptions.ProfissionalNotFoundException;
import br.edu.ufape.sguPraeService.models.Profissional;

import java.util.List;

public interface ProfissionalService {
    List<Profissional> listar();

    Profissional buscar(Long id) throws ProfissionalNotFoundException;

    Profissional salvar(Profissional entity);

    Profissional editar(Long id, Profissional entity) throws ProfissionalNotFoundException;

    void deletar(Long id) throws ProfissionalNotFoundException;
}
