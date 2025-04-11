package br.edu.ufape.sguPraeService.comunicacao.controllers;
import br.edu.ufape.sguPraeService.fachada.Fachada;
import br.edu.ufape.sguPraeService.models.TipoAuxilio;
import br.edu.ufape.sguPraeService.comunicacao.dto.tipoAuxilio.TipoAuxilioResponse;
import br.edu.ufape.sguPraeService.comunicacao.dto.tipoAuxilio.TipoAuxilioRequest;
import br.edu.ufape.sguPraeService.exceptions.TipoAuxilioNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import java.util.List;
import org.springframework.http.HttpStatus;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tipo-auxilio")
public class TipoAuxilioController {
    private final Fachada fachada;
    private final ModelMapper modelMapper;


    @GetMapping
    public List<TipoAuxilioResponse> listar() {
        return fachada.listarTipoAuxilios().stream().map(tipoAuxilio -> new TipoAuxilioResponse(tipoAuxilio, modelMapper)).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TipoAuxilioResponse> buscar(@PathVariable Long id) throws TipoAuxilioNotFoundException {
        TipoAuxilio response = fachada.buscarTipoAuxilio(id);
        return new ResponseEntity<>(new TipoAuxilioResponse(response, modelMapper), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<TipoAuxilioResponse> salvar(@Valid @RequestBody TipoAuxilioRequest entity) {
        TipoAuxilio response = fachada.salvarTipoAuxilio(entity.convertToEntity(entity, modelMapper));
        return new ResponseEntity<>(new TipoAuxilioResponse(response, modelMapper), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<TipoAuxilioResponse> editar(@PathVariable Long id, @Valid @RequestBody TipoAuxilioRequest entity) throws TipoAuxilioNotFoundException {
        TipoAuxilio response = fachada.editarTipoAuxilio(id, entity.convertToEntity(entity, modelMapper));
        return new ResponseEntity<>(new TipoAuxilioResponse(response, modelMapper), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) throws TipoAuxilioNotFoundException {
        fachada.deletarTipoAuxilio(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}


