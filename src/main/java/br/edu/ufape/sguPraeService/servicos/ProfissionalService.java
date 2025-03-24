package br.edu.ufape.sguPraeService.servicos;

import br.edu.ufape.sguPraeService.exceptions.ProfissionalNotFoundException;
import br.edu.ufape.sguPraeService.models.Profissional;
import br.edu.ufape.sguPraeService.dados.ProfissionalRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import java.util.List;

@Service @RequiredArgsConstructor
public class ProfissionalService implements br.edu.ufape.sguPraeService.servicos.interfaces.ProfissionalService {
    private final ProfissionalRepository repository;
    private final ModelMapper modelMapper;

    @Override
    public List<Profissional> listar() {
        return repository.findAll();
    }

    @Override
    public Profissional buscar(Long id) throws ProfissionalNotFoundException {
        return repository.findById(id).orElseThrow(ProfissionalNotFoundException::new);
    }

    @Override
    public Profissional salvar(Profissional entity) {
        return repository.save(entity);
    }

    @Override
    public Profissional editar(Long id, Profissional entity) throws ProfissionalNotFoundException {
        Profissional profissional = buscar(id);
        modelMapper.map(entity, profissional);
        return repository.save(profissional);

    }

    @Override
    public void deletar(Long id) throws ProfissionalNotFoundException {
        Profissional profissional = buscar(id);
        profissional.setAtivo(false);
        repository.save(profissional);
    }
}
