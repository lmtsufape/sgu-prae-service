package br.edu.ufape.sguPraeService.fachada;


import br.edu.ufape.sguPraeService.exceptions.ProfissionalNotFoundException;
import br.edu.ufape.sguPraeService.models.Profissional;
import br.edu.ufape.sguPraeService.servicos.interfaces.ProfissionalService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component @RequiredArgsConstructor
public class Fachada {
    private final ProfissionalService profissionalService;

    // ------------------- Profissional ------------------- //
    public List<Profissional> listarProfissionais() {
        return profissionalService.listar();
    }

    public Profissional buscarProfissional(Long id) throws ProfissionalNotFoundException {
        return profissionalService.buscar(id);
    }

    public Profissional salvarProfissional(Profissional profissional) {
        return profissionalService.salvar(profissional);
    }

    public Profissional editarProfissional(Long id, Profissional profissional) throws ProfissionalNotFoundException {
        return profissionalService.editar(id, profissional);
    }

    public void deletarProfissional(Long id) throws ProfissionalNotFoundException {
        profissionalService.deletar(id);
    }
}
