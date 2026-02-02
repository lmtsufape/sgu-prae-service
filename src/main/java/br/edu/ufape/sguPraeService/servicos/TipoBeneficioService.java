package br.edu.ufape.sguPraeService.servicos;

import br.edu.ufape.sguPraeService.dados.TipoBeneficioRepository;
import br.edu.ufape.sguPraeService.exceptions.TipoBeneficioNotFoundException;
import br.edu.ufape.sguPraeService.models.TipoBeneficio;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.BooleanBuilder;
import br.edu.ufape.sguPraeService.models.QTipoBeneficio;

@Service @RequiredArgsConstructor
public class TipoBeneficioService implements br.edu.ufape.sguPraeService.servicos.interfaces.TipoBeneficioService {
    private final TipoBeneficioRepository tipoBeneficioRepository;
    private final ModelMapper modelMapper;

    @Override
    public Page<TipoBeneficio> listar(Predicate predicate, Pageable pageable) {
        QTipoBeneficio qTipoBeneficio = QTipoBeneficio.tipoBeneficio;
        BooleanBuilder filtroBase = new BooleanBuilder();
        filtroBase.and(qTipoBeneficio.ativo.isTrue());

        Predicate predicadoFinal = filtroBase.and(predicate);
        return tipoBeneficioRepository.findAll(predicadoFinal, pageable);
    }

    @Override
    public TipoBeneficio buscar(Long id) throws TipoBeneficioNotFoundException {
        return tipoBeneficioRepository.findById(id).orElseThrow(TipoBeneficioNotFoundException::new);
    }

    @Override
    public TipoBeneficio salvar(TipoBeneficio entity) { return tipoBeneficioRepository.save(entity); }

    @Override
    public TipoBeneficio editar(Long id, TipoBeneficio entity) throws TipoBeneficioNotFoundException {
        TipoBeneficio tipoBeneficio = buscar(id);
        modelMapper.map(entity, tipoBeneficio);
        return tipoBeneficioRepository.save(tipoBeneficio);
    }

    @Override
    public void deletar(Long id) throws TipoBeneficioNotFoundException {
        TipoBeneficio tipoBeneficio = buscar(id);
        tipoBeneficioRepository.delete(tipoBeneficio);
    }

    @Override
    public void desativar(Long id) throws TipoBeneficioNotFoundException {
        TipoBeneficio tipoBeneficio = buscar(id);
        tipoBeneficio.setAtivo(false);
        tipoBeneficioRepository.save(tipoBeneficio);
    }

    @Override
    public Long contarTiposAtivos() {
        return tipoBeneficioRepository.countAtivos();
    }
}
