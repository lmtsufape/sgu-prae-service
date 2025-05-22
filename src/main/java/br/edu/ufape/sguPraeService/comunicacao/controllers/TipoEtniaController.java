package br.edu.ufape.sguPraeService.comunicacao.controllers;

import br.edu.ufape.sguPraeService.comunicacao.dto.tipoEtnia.TipoEtniaRequest;
import br.edu.ufape.sguPraeService.comunicacao.dto.tipoEtnia.TipoEtniaResponse;
import br.edu.ufape.sguPraeService.exceptions.notFoundExceptions.TipoEtniaNotFoundException;
import br.edu.ufape.sguPraeService.fachada.Fachada;
import br.edu.ufape.sguPraeService.models.TipoEtnia;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/tipoEtnia")
@RequiredArgsConstructor
public class TipoEtniaController {
    private final Fachada fachada;
    private final ModelMapper modelMapper;

    @GetMapping("/{id}")
    public ResponseEntity<TipoEtniaResponse> buscarTipoEtnia(@PathVariable Long id) throws TipoEtniaNotFoundException {
        TipoEtnia tipoEtnia = fachada.buscarTipoEtnia(id);
        if (tipoEtnia == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new TipoEtniaResponse(tipoEtnia, modelMapper), HttpStatus.OK);
    }

    @GetMapping
    public List<TipoEtniaResponse> listarTiposEtnia() {
        return fachada.listarTiposEtnia().stream()
                .map(tipoEtnia -> new TipoEtniaResponse(tipoEtnia, modelMapper))
                .collect(Collectors.toList());
    }

    @PreAuthorize("hasRole('GESTOR') and hasRole('PRAE_ACESS')")
    @PostMapping
    public ResponseEntity<TipoEtniaResponse> criarTipoEtnia(@RequestBody TipoEtniaRequest tipoEtniaRequest) {
        TipoEtnia tipoEtnia = tipoEtniaRequest.convertToEntity(tipoEtniaRequest, modelMapper);
        TipoEtnia novoTipoEtnia = fachada.salvarTipoEtnia(tipoEtnia);
        return new ResponseEntity<>(new TipoEtniaResponse(novoTipoEtnia, modelMapper), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('GESTOR') and hasRole('PRAE_ACESS')")
    @PatchMapping("/{id}")
    public ResponseEntity<TipoEtniaResponse> atualizarTipoEtnia(@PathVariable Long id, @RequestBody TipoEtniaRequest tipoEtniaRequest) throws TipoEtniaNotFoundException {
        TipoEtnia tipoEtnia = tipoEtniaRequest.convertToEntity(tipoEtniaRequest, modelMapper);
        TipoEtnia tipoEtniaAtualizado = fachada.atualizarTipoEtnia(id, tipoEtnia);
        if (tipoEtniaAtualizado == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new TipoEtniaResponse(tipoEtniaAtualizado, modelMapper), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('GESTOR') and hasRole('PRAE_ACESS')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarTipoEtnia(@PathVariable Long id) throws TipoEtniaNotFoundException {
        fachada.deletarTipoEtnia(id);
        return ResponseEntity.noContent().build();
    }
}