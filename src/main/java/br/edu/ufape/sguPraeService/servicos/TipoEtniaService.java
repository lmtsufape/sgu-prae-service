package br.edu.ufape.sguPraeService.servicos;

import br.edu.ufape.sguPraeService.dados.TipoEtniaRepository;
import br.edu.ufape.sguPraeService.exceptions.TipoEtniaNotFoundException;
import br.edu.ufape.sguPraeService.models.TipoEtnia;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TipoEtniaService implements br.edu.ufape.sguPraeService.servicos.interfaces.TipoEtniaService {
    private final TipoEtniaRepository tipoEtniaRepository;

    @Override
    public TipoEtnia salvarTipoEtnia(TipoEtnia tipoEtnia) {
        if (tipoEtnia.getTipo() == null || tipoEtnia.getTipo().trim().isEmpty()) {
            throw new IllegalArgumentException("O tipo da etnia é obrigatório.");
        }
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

    @Override
    public TipoEtnia atualizarTipoEtnia(Long id, TipoEtnia tipoEtnia) throws TipoEtniaNotFoundException {
        TipoEtnia tipoEtniaExistente = tipoEtniaRepository.findById(id).orElseThrow(TipoEtniaNotFoundException::new);
        if (tipoEtnia.getTipo() == null || tipoEtnia.getTipo().trim().isEmpty()) {
            throw new IllegalArgumentException("O tipo da etnia é obrigatório.");
        }
        tipoEtniaExistente.setTipo(tipoEtnia.getTipo());
        return tipoEtniaRepository.save(tipoEtniaExistente);
    }

    @Override
    public void deletarTipoEtnia(Long id) throws TipoEtniaNotFoundException {
        if (!tipoEtniaRepository.existsById(id)) {
            throw new TipoEtniaNotFoundException();
        }
        tipoEtniaRepository.deleteById(id);
    }
}
