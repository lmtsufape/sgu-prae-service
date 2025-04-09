package br.edu.ufape.sguPraeService.servicos;

import br.edu.ufape.sguPraeService.exceptions.ExceptionUtil;
import br.edu.ufape.sguPraeService.exceptions.notFoundExceptions.ProfissionalNotFoundException;
import br.edu.ufape.sguPraeService.models.Profissional;
import br.edu.ufape.sguPraeService.dados.ProfissionalRepository;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
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
    public Profissional buscarPorUserId(String id) throws ProfissionalNotFoundException {
        return repository.findByUserId(id).orElseThrow(ProfissionalNotFoundException::new);
    }

    @Override
    public Profissional salvar(Profissional entity) {
        try {
            return repository.save(entity);
        }catch (DataIntegrityViolationException e){
            throw ExceptionUtil.handleDataIntegrityViolationException(e);
        }

    }

    @Override
    public Profissional editar(String userId, Profissional entity) throws ProfissionalNotFoundException {
        try{
            Profissional profissional = repository.findByUserId(userId).orElseThrow(ProfissionalNotFoundException::new);
            modelMapper.map(entity, profissional);
            return repository.save(profissional);
        }catch (DataIntegrityViolationException e){
            throw ExceptionUtil.handleDataIntegrityViolationException(e);
        }

    }

    @Override
    public void deletar(Long id) throws ProfissionalNotFoundException {
        Profissional profissional = buscar(id);
        profissional.setAtivo(false);
        repository.save(profissional);
    }
}
