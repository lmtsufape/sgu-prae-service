package br.edu.ufape.sguPraeService.comunicacao.controllers;
import br.edu.ufape.sguPraeService.fachada.Fachada;
import br.edu.ufape.sguPraeService.models.TipoBolsa;
import br.edu.ufape.sguPraeService.comunicacao.dto.tipoBolsa.TipoBolsaResponse;
import br.edu.ufape.sguPraeService.comunicacao.dto.tipoBolsa.TipoBolsaRequest;
import br.edu.ufape.sguPraeService.exceptions.TipoBolsaNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import java.util.List;
import org.springframework.http.HttpStatus;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tipo-bolsa")
public class TipoBolsaController {
    private final Fachada fachada;
    private final ModelMapper modelMapper;


    @GetMapping
    public List<TipoBolsaResponse> listar() {
        return fachada.listarTipoBolsas().stream().map(tipoBolsa -> new TipoBolsaResponse(tipoBolsa, modelMapper)).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TipoBolsaResponse> buscar(@PathVariable Long id) throws TipoBolsaNotFoundException {
        TipoBolsa response = fachada.buscarTipoBolsa(id);
        return new ResponseEntity<>(new TipoBolsaResponse(response, modelMapper), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('GESTOR') and hasRole('PRAE_ACESS')")
    @PostMapping
    public ResponseEntity<TipoBolsaResponse> salvar(@Valid @RequestBody TipoBolsaRequest entity) {
        TipoBolsa response = fachada.salvarTipoBolsa(entity.convertToEntity(entity, modelMapper));
        return new ResponseEntity<>(new TipoBolsaResponse(response, modelMapper), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('GESTOR') and hasRole('PRAE_ACESS')")
    @PatchMapping("/{id}")
    public ResponseEntity<TipoBolsaResponse> editar(@PathVariable Long id, @Valid @RequestBody TipoBolsaRequest entity) throws TipoBolsaNotFoundException {
        TipoBolsa response = fachada.editarTipoBolsa(id, entity.convertToEntity(entity, modelMapper));
        return new ResponseEntity<>(new TipoBolsaResponse(response, modelMapper), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('GESTOR') and hasRole('PRAE_ACESS')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) throws TipoBolsaNotFoundException {
        fachada.deletarTipoBolsa(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PreAuthorize("hasRole('GESTOR') and hasRole('PRAE_ACESS')")
    @PatchMapping("desativar/{id}")
    public ResponseEntity<Void> desativar(@PathVariable Long id) throws TipoBolsaNotFoundException {
        fachada.desativarTipoBolsa(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

