 package br.edu.ufape.sguPraeService.comunicacao.controllers;
 
 import br.edu.ufape.sguPraeService.models.Vaga;
 import br.edu.ufape.sguPraeService.servicos.VagaService;
 import br.edu.ufape.sguPraeService.comunicacao.dto.vaga.VagaResponse;
 import br.edu.ufape.sguPraeService.comunicacao.dto.vaga.VagaRequest;
 import br.edu.ufape.sguPraeService.exceptions.notFoundExceptions.VagaNotFoundException;
 
 
 import org.modelmapper.ModelMapper;
 import org.springframework.web.bind.annotation.*;
 import org.springframework.http.ResponseEntity;
 
 import jakarta.validation.Valid;
 import lombok.RequiredArgsConstructor;
 import java.util.List;
 import org.springframework.http.HttpStatus;
 
 @RestController
 @RequiredArgsConstructor
 @RequestMapping("/vaga")
 public class VagaController {
     private final VagaService service;
     private final ModelMapper modelMapper;
 
 
     @GetMapping
     public List<VagaResponse> listar() {
         return service.listar().stream().map(vaga -> new VagaResponse(vaga, modelMapper)).toList();
     }
 
     @GetMapping("/{id}")
     public ResponseEntity<VagaResponse> buscar(@PathVariable Long id) throws VagaNotFoundException {
         Vaga response = service.buscar(id);
         return new ResponseEntity<>(new VagaResponse(response, modelMapper), HttpStatus.OK);
     }

     @PatchMapping("/{id}")
     public ResponseEntity<VagaResponse> editar(@PathVariable Long id, @Valid @RequestBody VagaRequest entity) throws VagaNotFoundException {
         Vaga response = service.editar(id, entity.convertToEntity(entity, modelMapper));
         return new ResponseEntity<>(new VagaResponse(response, modelMapper), HttpStatus.OK);
     }
 
     @DeleteMapping("/{id}")
     public ResponseEntity<Void> delete(@PathVariable Long id) {
         service.deletar(id);
         return new ResponseEntity<>(HttpStatus.NO_CONTENT);
     }

 }
 