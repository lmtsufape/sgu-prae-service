package br.edu.ufape.sguPraeService.servicos.interfaces;

import br.edu.ufape.sguPraeService.models.TipoEtnia;
import java.util.List;

public interface TipoEtniaService {
    TipoEtnia salvarTipoEtnia(TipoEtnia tipoEtnia);

    TipoEtnia buscarTipoEtnia(Long id);

    List<TipoEtnia> listarTiposEtnia();

    TipoEtnia atualizarTipoEtnia(Long id, TipoEtnia tipoEtnia);

    void deletarTipoEtnia(Long id);
}