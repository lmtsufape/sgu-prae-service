package br.edu.ufape.sguPraeService.servicos;

import br.edu.ufape.sguPraeService.dados.TipoAuxilioRepository;
import br.edu.ufape.sguPraeService.exceptions.TipoAuxilioNotFoundException;
import br.edu.ufape.sguPraeService.models.TipoAuxilio;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.util.List;

@Service @RequiredArgsConstructor
public class TipoAuxilioService implements br.edu.ufape.sguPraeService.servicos.interfaces.TipoAuxilioService {
    private final TipoAuxilioRepository tipoAuxilioRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<TipoAuxilio> listar() { return tipoAuxilioRepository.findAll(); }

    @Override
    public TipoAuxilio buscar(Long id) throws TipoAuxilioNotFoundException {
        return tipoAuxilioRepository.findById(id).orElseThrow(TipoAuxilioNotFoundException::new);
    }

    @Override
    public TipoAuxilio salvar(TipoAuxilio entity) { return tipoAuxilioRepository.save(entity); }

    @Override
    public TipoAuxilio editar(Long id, TipoAuxilio entity) throws TipoAuxilioNotFoundException {
        TipoAuxilio tipoAuxilio = buscar(id);
        modelMapper.map(entity, tipoAuxilio);
        return tipoAuxilioRepository.save(tipoAuxilio);
    }

    @Override
    public void deletar(Long id) throws TipoAuxilioNotFoundException {
        TipoAuxilio tipoAuxilio = buscar(id);
        tipoAuxilio.setAtivo(false);
        tipoAuxilioRepository.save(tipoAuxilio);
    }
}
