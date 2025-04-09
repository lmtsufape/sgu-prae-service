package br.edu.ufape.sguPraeService.servicos.interfaces;

import br.edu.ufape.sguPraeService.exceptions.notFoundExceptions.VagaNotFoundException;
import br.edu.ufape.sguPraeService.models.Vaga;

import java.time.LocalTime;
import java.util.List;

public interface VagaService {
    List<Vaga> listar();

    Vaga buscar(Long id) throws VagaNotFoundException;

    Vaga editar(Long id, Vaga entity) throws VagaNotFoundException;

    List<Vaga> gerarVagas(List<LocalTime> horarios, LocalTime tempoAtendimento);

    void deletar(Long id);
}
