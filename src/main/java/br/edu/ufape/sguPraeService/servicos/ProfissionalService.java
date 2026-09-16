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
import java.util.UUID;

@Service @RequiredArgsConstructor
public class ProfissionalService implements br.edu.ufape.sguPraeService.servicos.interfaces.ProfissionalService {
    private final ProfissionalRepository repository;
    private final ModelMapper modelMapper;

    @Override
    public List<Profissional> listar() {
        return repository.findAll();
    }

    @Override
    public Profissional buscar(UUID id) throws ProfissionalNotFoundException {
        return repository.findById(id).orElseThrow(ProfissionalNotFoundException::new);
    }

    @Override
    public Profissional buscarPorUserId(UUID id) throws ProfissionalNotFoundException {
        // ATUALIZADO: Como userId virou PK, chamamos apenas o findById nativo
        return repository.findById(id).orElseThrow(ProfissionalNotFoundException::new);
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
    public Profissional editar(UUID userId, Profissional entity) throws ProfissionalNotFoundException {
        try{
            // ATUALIZADO: Usando findById nativo em vez de findByUserId
            Profissional profissional = repository.findById(userId).orElseThrow(ProfissionalNotFoundException::new);
            modelMapper.map(entity, profissional);
            return repository.save(profissional);
        }catch (DataIntegrityViolationException e){
            throw ExceptionUtil.handleDataIntegrityViolationException(e);
        }

    }

    @Override
    public void deletar(UUID id) throws ProfissionalNotFoundException {
        Profissional profissional = buscar(id);
        profissional.setAtivo(false);
        repository.save(profissional);
    }
}