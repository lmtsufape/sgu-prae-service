package br.edu.ufape.sguPraeService.comunicacao.controllers;
import br.edu.ufape.sguPraeService.fachada.Fachada;
import br.edu.ufape.sguPraeService.models.Auxilio;
import br.edu.ufape.sguPraeService.comunicacao.dto.auxilio.AuxilioResponse;
import br.edu.ufape.sguPraeService.comunicacao.dto.auxilio.AuxilioRequest;
import br.edu.ufape.sguPraeService.exceptions.AuxilioNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import java.util.List;
import org.springframework.http.HttpStatus;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auxilio")
public class AuxilioController {
    private final Fachada fachada;
    private final ModelMapper modelMapper;


    @GetMapping
    public List<AuxilioResponse> listar() {
        return fachada.listarAuxilios().stream().map(auxilio -> new AuxilioResponse(auxilio, modelMapper)).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuxilioResponse> buscar(@PathVariable Long id) throws AuxilioNotFoundException {
        Auxilio response = fachada.buscarAuxilio(id);
        return new ResponseEntity<>(new AuxilioResponse(response, modelMapper), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<AuxilioResponse> salvar(@Valid @RequestBody AuxilioRequest entity) {
        Auxilio response = fachada.salvarAuxilio(entity.convertToEntity(entity, modelMapper));
        return new ResponseEntity<>(new AuxilioResponse(response, modelMapper), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<AuxilioResponse> editar(@PathVariable Long id, @Valid @RequestBody AuxilioRequest entity) throws AuxilioNotFoundException {
        Auxilio response = fachada.editarAuxilio(id, entity.convertToEntity(entity, modelMapper));
        return new ResponseEntity<>(new AuxilioResponse(response, modelMapper), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) throws AuxilioNotFoundException {
        fachada.deletarAuxilio(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
