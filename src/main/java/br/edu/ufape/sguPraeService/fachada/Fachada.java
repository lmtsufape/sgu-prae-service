package br.edu.ufape.sguPraeService.fachada;


import br.edu.ufape.sguPraeService.exceptions.AuxilioNotFoundException;
import br.edu.ufape.sguPraeService.exceptions.ProfissionalNotFoundException;
import br.edu.ufape.sguPraeService.exceptions.TipoAuxilioNotFoundException;
import br.edu.ufape.sguPraeService.exceptions.TipoBolsaNotFoundException;
import br.edu.ufape.sguPraeService.models.Auxilio;
import br.edu.ufape.sguPraeService.models.Profissional;
import br.edu.ufape.sguPraeService.models.TipoAuxilio;
import br.edu.ufape.sguPraeService.models.TipoBolsa;
import br.edu.ufape.sguPraeService.servicos.interfaces.AuxilioService;
import br.edu.ufape.sguPraeService.servicos.interfaces.ProfissionalService;
import br.edu.ufape.sguPraeService.servicos.interfaces.TipoAuxilioService;
import br.edu.ufape.sguPraeService.servicos.interfaces.TipoBolsaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component @RequiredArgsConstructor
public class Fachada {
    private final ProfissionalService profissionalService;
    private final TipoBolsaService tipoBolsaService;
    private final TipoAuxilioService tipoAuxilioService;
    private final AuxilioService auxilioService;

    // ------------------- Profissional ------------------- //
    public List<Profissional> listarProfissionais() {
        return profissionalService.listar();
    }

    public Profissional buscarProfissional(Long id) throws ProfissionalNotFoundException {
        return profissionalService.buscar(id);
    }

    public Profissional salvarProfissional(Profissional profissional) {
        return profissionalService.salvar(profissional);
    }

    public Profissional editarProfissional(Long id, Profissional profissional) throws ProfissionalNotFoundException {
        return profissionalService.editar(id, profissional);
    }

    public void deletarProfissional(Long id) throws ProfissionalNotFoundException {
        profissionalService.deletar(id);
    }

    // ------------------- TipoBolsa ------------------- //
    public List<TipoBolsa> listarTipoBolsas() {
        return tipoBolsaService.listar();
    }

    public TipoBolsa buscarTipoBolsa(Long id) throws TipoBolsaNotFoundException {
        return tipoBolsaService.buscar(id);
    }

    public TipoBolsa salvarTipoBolsa(TipoBolsa tipoBolsa) {
        return tipoBolsaService.salvar(tipoBolsa);
    }

    public TipoBolsa editarTipoBolsa(Long id, TipoBolsa tipoBolsa) throws TipoBolsaNotFoundException {
        return tipoBolsaService.editar(id, tipoBolsa);
    }

    public void deletarTipoBolsa(Long id) throws TipoBolsaNotFoundException {
        tipoBolsaService.deletar(id);
    }

    // ------------------- TipoAuxilio ------------------- //
    public List<TipoAuxilio> listarTipoAuxilios() {
        return tipoAuxilioService.listar();
    }

    public TipoAuxilio buscarTipoAuxilio(Long id) throws TipoAuxilioNotFoundException {
        return tipoAuxilioService.buscar(id);
    }

    public TipoAuxilio salvarTipoAuxilio(TipoAuxilio tipoAuxilio) {
        return tipoAuxilioService.salvar(tipoAuxilio);
    }

    public TipoAuxilio editarTipoAuxilio(Long id, TipoAuxilio tipoAuxilio) throws TipoAuxilioNotFoundException {
        return tipoAuxilioService.editar(id, tipoAuxilio);
    }

    public void deletarTipoAuxilio(Long id) throws TipoAuxilioNotFoundException {
        tipoAuxilioService.deletar(id);
    }

    // ------------------- Auxilio ------------------- //
    public List<Auxilio> listarAuxilios() {
        return auxilioService.listar();
    }

    public Auxilio buscarAuxilio(Long id) throws AuxilioNotFoundException {
        return auxilioService.buscar(id);
    }

    public Auxilio salvarAuxilio(Auxilio auxilio) {
        return auxilioService.salvar(auxilio);
    }

    public Auxilio editarAuxilio(Long id, Auxilio auxilio) throws AuxilioNotFoundException {
        return auxilioService.editar(id, auxilio);
    }

    public void deletarAuxilio(Long id) throws AuxilioNotFoundException {
        auxilioService.deletar(id);
    }
}
