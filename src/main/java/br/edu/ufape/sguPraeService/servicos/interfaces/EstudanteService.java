package br.edu.ufape.sguPraeService.servicos.interfaces;

import br.edu.ufape.sguPraeService.exceptions.EstudanteNotFoundException;
import br.edu.ufape.sguPraeService.models.Estudante;
import java.util.List;

public interface EstudanteService {
    Estudante salvarEstudante(Estudante estudante);

    Estudante buscarEstudante(Long id) throws EstudanteNotFoundException;

    List<Estudante> listarEstudantes();

    Estudante atualizarEstudante(Long id, Estudante estudante) throws EstudanteNotFoundException;

    void deletarEstudante(Long id);
}