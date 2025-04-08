package br.edu.ufape.sguPraeService.servicos;

import br.edu.ufape.sguPraeService.dados.TipoEtniaRepository;
import br.edu.ufape.sguPraeService.exceptions.ExceptionUtil;
import br.edu.ufape.sguPraeService.exceptions.notFoundExceptions.TipoEtniaNotFoundException;
import br.edu.ufape.sguPraeService.models.TipoEtnia;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TipoEtniaService implements br.edu.ufape.sguPraeService.servicos.interfaces.TipoEtniaService {
    private final TipoEtniaRepository tipoEtniaRepository;

    @Override
    public TipoEtnia salvarTipoEtnia(TipoEtnia tipoEtnia) {
        try {
            return tipoEtniaRepository.save(tipoEtnia);
        }catch (DataIntegrityViolationException e){
            throw ExceptionUtil.handleDataIntegrityViolationException(e);
        }

    }

    @Override
    public TipoEtnia buscarTipoEtnia(Long id) throws TipoEtniaNotFoundException {
        return tipoEtniaRepository.findById(id).orElseThrow(TipoEtniaNotFoundException::new);
    }

    @Override
    public List<TipoEtnia> listarTiposEtnia() {
        return tipoEtniaRepository.findAll();
    }

    @Override
    public TipoEtnia atualizarTipoEtnia(Long id, TipoEtnia tipoEtnia) throws TipoEtniaNotFoundException {
        try {
            TipoEtnia tipoEtniaExistente = tipoEtniaRepository.findById(id).orElseThrow(TipoEtniaNotFoundException::new);
            tipoEtniaExistente.setTipo(tipoEtnia.getTipo());
            return tipoEtniaRepository.save(tipoEtniaExistente);
        }catch (DataIntegrityViolationException e){
            throw ExceptionUtil.handleDataIntegrityViolationException(e);
        }

    }

    @Override
    public void deletarTipoEtnia(Long id) throws TipoEtniaNotFoundException {
        if (!tipoEtniaRepository.existsById(id)) {
            throw new TipoEtniaNotFoundException();
        }
        tipoEtniaRepository.deleteById(id);
    }
}
