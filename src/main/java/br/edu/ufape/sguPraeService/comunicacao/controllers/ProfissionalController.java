 package br.edu.ufape.sguPraeService.comunicacao.controllers;
 
 import br.edu.ufape.sguPraeService.fachada.Fachada;
 import br.edu.ufape.sguPraeService.models.Profissional;
 import br.edu.ufape.sguPraeService.comunicacao.dto.profissional.ProfissionalResponse;
 import br.edu.ufape.sguPraeService.comunicacao.dto.profissional.ProfissionalRequest;
 import br.edu.ufape.sguPraeService.exceptions.ProfissionalNotFoundException;
 
 
 import org.modelmapper.ModelMapper;
 import org.springframework.web.bind.annotation.*;
 import org.springframework.http.ResponseEntity;
 
 import jakarta.validation.Valid;
 import lombok.RequiredArgsConstructor;
 import java.util.List;

 import org.springframework.http.HttpStatus;
 
 @RestController
 @RequiredArgsConstructor
 @RequestMapping("/profissional")
 public class ProfissionalController {
     private final Fachada fachada;
     private final ModelMapper modelMapper;
 
 
     @GetMapping
     public List<ProfissionalResponse> listar() {
         return fachada.listarProfissionais().stream().map(profissional -> new ProfissionalResponse(profissional, modelMapper)).toList();
     }
 
     @GetMapping("/{id}")
     public ResponseEntity<ProfissionalResponse> buscar(@PathVariable Long id) throws ProfissionalNotFoundException {
         Profissional response = fachada.buscarProfissional(id);
         return new ResponseEntity<>(new ProfissionalResponse(response, modelMapper), HttpStatus.OK);
     }
 
     @PostMapping
     public ResponseEntity<ProfissionalResponse> salvar(@Valid @RequestBody ProfissionalRequest entity) {
         Profissional response = fachada.salvarProfissional(entity.convertToEntity(entity, modelMapper));
         return new ResponseEntity<>(new ProfissionalResponse(response, modelMapper), HttpStatus.CREATED);
     }
 
     @PatchMapping("/{id}")
     public ResponseEntity<ProfissionalResponse> editar(@PathVariable Long id, @Valid @RequestBody ProfissionalRequest entity) throws ProfissionalNotFoundException {
         Profissional response = fachada.editarProfissional(id, entity.convertToEntity(entity, modelMapper));
         return new ResponseEntity<>(new ProfissionalResponse(response, modelMapper), HttpStatus.OK);
     }
 
     @DeleteMapping("/{id}")
     public ResponseEntity<Void> delete(@PathVariable Long id) throws ProfissionalNotFoundException {
         fachada.deletarProfissional(id);
         return new ResponseEntity<>(HttpStatus.NO_CONTENT);
     }
 }
 