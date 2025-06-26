package br.edu.ufape.sguPraeService.servicos.interfaces;

import br.edu.ufape.sguPraeService.exceptions.TipoBeneficioNotFoundException;
import br.edu.ufape.sguPraeService.models.TipoBeneficio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface TipoBeneficioService {
    Page<TipoBeneficio> listar(Pageable pageable);

    TipoBeneficio buscar(Long id) throws TipoBeneficioNotFoundException;

    TipoBeneficio salvar(TipoBeneficio entity);

    TipoBeneficio editar(Long id, TipoBeneficio entity) throws TipoBeneficioNotFoundException;

    void deletar(Long id) throws TipoBeneficioNotFoundException;

    void desativar(Long id) throws TipoBeneficioNotFoundException;
}
