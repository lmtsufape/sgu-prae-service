package br.edu.ufape.sguPraeService.servicos;

import br.edu.ufape.sguPraeService.exceptions.TipoBolsaNotFoundException;
import br.edu.ufape.sguPraeService.dados.TipoBolsaRepository;
import br.edu.ufape.sguPraeService.models.TipoBolsa;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.util.List;

@Service @RequiredArgsConstructor
public class TipoBolsaService implements br.edu.ufape.sguPraeService.servicos.interfaces.TipoBolsaService {
    private final TipoBolsaRepository tipoBolsaRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<TipoBolsa> listar() { return tipoBolsaRepository.findAll(); }

    @Override
    public TipoBolsa buscar(Long id) throws TipoBolsaNotFoundException {
        return tipoBolsaRepository.findById(id).orElseThrow(TipoBolsaNotFoundException::new);
    }

    @Override
    public TipoBolsa salvar(TipoBolsa entity) { return tipoBolsaRepository.save(entity); }

    @Override
    public TipoBolsa editar(Long id, TipoBolsa entity) throws TipoBolsaNotFoundException {
        TipoBolsa tipoBolsa = buscar(id);
        modelMapper.map(entity, tipoBolsa);
        return tipoBolsaRepository.save(tipoBolsa);
    }

    @Override
    public void deletar(Long id) throws TipoBolsaNotFoundException {
        TipoBolsa tipoBolsa = buscar(id);
        tipoBolsa.setAtivo(false);
        tipoBolsaRepository.save(tipoBolsa);
    }
}
