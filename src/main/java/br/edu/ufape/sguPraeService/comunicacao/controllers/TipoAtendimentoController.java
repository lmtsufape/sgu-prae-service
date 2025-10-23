 package br.edu.ufape.sguPraeService.comunicacao.controllers;
 
 import br.edu.ufape.sguPraeService.comunicacao.dto.tipoatendimento.TipoAtendimentoUpdateRequest;
 import br.edu.ufape.sguPraeService.fachada.Fachada;
 import br.edu.ufape.sguPraeService.models.TipoAtendimento;
 import br.edu.ufape.sguPraeService.comunicacao.dto.tipoatendimento.TipoAtendimentoResponse;
 import br.edu.ufape.sguPraeService.comunicacao.dto.tipoatendimento.TipoAtendimentoRequest;
 import br.edu.ufape.sguPraeService.exceptions.notFoundExceptions.TipoAtendimentoNotFoundException;
 
 
 import org.modelmapper.ModelMapper;
 import org.springframework.data.domain.Page;
 import org.springframework.data.domain.Pageable;
 import org.springframework.data.web.PageableDefault;
 import org.springframework.security.access.prepost.PreAuthorize;
 import org.springframework.web.bind.annotation.*;
 import org.springframework.http.ResponseEntity;
 import com.querydsl.core.types.Predicate;
 import org.springframework.data.querydsl.binding.QuerydslPredicate;
 
 import jakarta.validation.Valid;
 import lombok.RequiredArgsConstructor;
 import org.springframework.http.HttpStatus;
 
 @RestController
 @RequiredArgsConstructor
 @RequestMapping("/tipo-atendimento")
 public class TipoAtendimentoController {
     private final Fachada fachada;
     private final ModelMapper modelMapper;


     @GetMapping
     public Page<TipoAtendimentoResponse> listar(
             @QuerydslPredicate(root = TipoAtendimento.class) Predicate predicate,
             @PageableDefault (sort = "id") Pageable pageable) {
         return fachada.listarTipoAtendimentos(predicate, pageable)
                 .map(tipoAtendimento -> new TipoAtendimentoResponse(tipoAtendimento, modelMapper));
     }
 
     @GetMapping("/{id}")
     public ResponseEntity<TipoAtendimentoResponse> buscar(@PathVariable Long id) throws TipoAtendimentoNotFoundException {
         TipoAtendimento response = fachada.buscarTipoAtendimento(id);
         return new ResponseEntity<>(new TipoAtendimentoResponse(response, modelMapper), HttpStatus.OK);
     }

     @PreAuthorize("hasRole('PROFISSIONAL')")
     @PostMapping
     public ResponseEntity<TipoAtendimentoResponse> salvar(@Valid @RequestBody TipoAtendimentoRequest entity) {
         TipoAtendimento response = fachada.salvarTipoAtendimento(entity.convertToEntity(entity, modelMapper));
         return new ResponseEntity<>(new TipoAtendimentoResponse(response, modelMapper), HttpStatus.CREATED);
     }

     @PreAuthorize("hasRole('PROFISSIONAL')")
     @PatchMapping("/{id}")
     public ResponseEntity<TipoAtendimentoResponse> editar(@PathVariable Long id, @Valid @RequestBody TipoAtendimentoUpdateRequest dto) throws TipoAtendimentoNotFoundException {
         TipoAtendimento response = fachada.editarTipoAtendimento(id, dto);
         return new ResponseEntity<>(new TipoAtendimentoResponse(response, modelMapper), HttpStatus.OK);
     }

     @PreAuthorize("hasRole('PROFISSIONAL')")
     @DeleteMapping("/{id}/horarios/{index}")
        public ResponseEntity<TipoAtendimentoResponse> deleteHorario(@PathVariable Long id, @PathVariable int index) throws TipoAtendimentoNotFoundException {
            TipoAtendimento response = fachada.deletarHorarioTipoAtendimento(id, index);
            return new ResponseEntity<>(new TipoAtendimentoResponse(response, modelMapper), HttpStatus.OK);
        }


        @PreAuthorize("hasRole('PROFISSIONAL')")
     @DeleteMapping("/{id}")
     public ResponseEntity<Void> delete(@PathVariable Long id) throws TipoAtendimentoNotFoundException {
         fachada.deletarTipoAtendimento(id);
         return new ResponseEntity<>(HttpStatus.NO_CONTENT);
     }
 }
 