 package br.edu.ufape.sguPraeService.comunicacao.controllers;
 
 import br.edu.ufape.sguPraeService.fachada.Fachada;
 import br.edu.ufape.sguPraeService.comunicacao.dto.profissional.ProfissionalResponse;
 import br.edu.ufape.sguPraeService.comunicacao.dto.profissional.ProfissionalRequest;
 import br.edu.ufape.sguPraeService.exceptions.notFoundExceptions.ProfissionalNotFoundException;
 
 
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
 @RequestMapping("/profissional")
 public class ProfissionalController {
     private final Fachada fachada;
     private final ModelMapper modelMapper;
 
 
     @GetMapping
     public List<ProfissionalResponse> listar() {
         return fachada.listarProfissionais();
     }
 
     @GetMapping("/{id}")
     public ResponseEntity<ProfissionalResponse> buscar(@PathVariable Long id) throws ProfissionalNotFoundException {
         return ResponseEntity.ok(fachada.buscarProfissional(id));
     }

     @PreAuthorize("(hasAnyRole('TECNICO', 'PROFESSOR')) and hasRole('PRAE_ACESS')")
     @PostMapping
     public ResponseEntity<ProfissionalResponse> salvar(@Valid @RequestBody ProfissionalRequest entity) {

            return ResponseEntity.status(HttpStatus.CREATED).
                    body(modelMapper.map(fachada.salvarProfissional(entity.convertToEntity(entity, modelMapper)),
                            ProfissionalResponse.class));
     }

     @PreAuthorize("hasRole('PROFISSIONAL')")
     @PatchMapping
     public ResponseEntity<ProfissionalResponse> editar(@Valid @RequestBody ProfissionalRequest entity) throws ProfissionalNotFoundException {
         ProfissionalResponse response = fachada.editarProfissional(entity.convertToEntity(entity, modelMapper));
         return ResponseEntity.ok(response);
     }

     @DeleteMapping("/{id}")
     public ResponseEntity<Void> delete(@PathVariable Long id) throws ProfissionalNotFoundException {
         fachada.deletarProfissional(id);
         return new ResponseEntity<>(HttpStatus.NO_CONTENT);
     }

 }
 