package br.edu.ufape.sguPraeService.servicos;

import br.edu.ufape.sguPraeService.dados.TipoEtniaRepository;
import br.edu.ufape.sguPraeService.exceptions.ExceptionUtil;
import br.edu.ufape.sguPraeService.exceptions.TipoEtniaDuplicadoException;
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

//    @Override
//    public TipoEtnia salvarTipoEtnia(TipoEtnia tipoEtnia) {
//        try {
//            return tipoEtniaRepository.save(tipoEtnia);
//        }catch (DataIntegrityViolationException e){
//            throw ExceptionUtil.handleDataIntegrityViolationException(e);
//        }
//
//    }

    @Override
    public TipoEtnia salvarTipoEtnia(TipoEtnia tipoEtnia) {
        tipoEtniaRepository.findByTipo(tipoEtnia.getTipo())
                .ifPresent(et -> {
                    throw new TipoEtniaDuplicadoException("Já existe um tipo de etnia: " + tipoEtnia.getTipo());
                });

        return tipoEtniaRepository.save(tipoEtnia);
    }

    @Override
    public TipoEtnia buscarTipoEtnia(Long id) throws TipoEtniaNotFoundException {
        return tipoEtniaRepository.findById(id).orElseThrow(TipoEtniaNotFoundException::new);
    }

    @Override
    public List<TipoEtnia> listarTiposEtnia() {
        return tipoEtniaRepository.findAll();
    }

//    @Override
//    public TipoEtnia atualizarTipoEtnia(Long id, TipoEtnia tipoEtnia) throws TipoEtniaNotFoundException {
//        try {
//            TipoEtnia tipoEtniaExistente = tipoEtniaRepository.findById(id).orElseThrow(TipoEtniaNotFoundException::new);
//            tipoEtniaExistente.setTipo(tipoEtnia.getTipo());
//            return tipoEtniaRepository.save(tipoEtniaExistente);
//        }catch (DataIntegrityViolationException e){
//            throw ExceptionUtil.handleDataIntegrityViolationException(e);
//        }
//
//    }

    @Override
    public TipoEtnia atualizarTipoEtnia(Long id, TipoEtnia tipoEtnia) throws TipoEtniaNotFoundException {
        TipoEtnia tipoEtniaExistente = tipoEtniaRepository.findById(id)
                .orElseThrow(TipoEtniaNotFoundException::new);

        tipoEtniaRepository.findByTipo(tipoEtnia.getTipo())
                .ifPresent(et -> {
                    if (!et.getId().equals(id)) {
                        throw new TipoEtniaDuplicadoException("Já existe um tipo de etnia: " + tipoEtnia.getTipo());
                    }
                });

        tipoEtniaExistente.setTipo(tipoEtnia.getTipo());
        return tipoEtniaRepository.save(tipoEtniaExistente);
    }

    @Override
    public void deletarTipoEtnia(Long id) throws TipoEtniaNotFoundException {
        tipoEtniaRepository.findById(id).orElseThrow(TipoEtniaNotFoundException::new);
        tipoEtniaRepository.deleteById(id);
    }
}
