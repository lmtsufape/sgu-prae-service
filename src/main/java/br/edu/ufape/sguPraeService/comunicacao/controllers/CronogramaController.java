 package br.edu.ufape.sguPraeService.comunicacao.controllers;

 import br.edu.ufape.sguPraeService.comunicacao.dto.cronograma.CronogramaUpdateRequest;
 import br.edu.ufape.sguPraeService.exceptions.notFoundExceptions.TipoAtendimentoNotFoundException;
 import br.edu.ufape.sguPraeService.fachada.Fachada;
 import br.edu.ufape.sguPraeService.models.Cronograma;
 import br.edu.ufape.sguPraeService.comunicacao.dto.cronograma.CronogramaResponse;
 import br.edu.ufape.sguPraeService.comunicacao.dto.cronograma.CronogramaRequest;
 import br.edu.ufape.sguPraeService.exceptions.notFoundExceptions.CronogramaNotFoundException;

 import org.modelmapper.ModelMapper;
 import org.springframework.data.domain.Page;
 import org.springframework.data.domain.Pageable;
 import org.springframework.data.web.PageableDefault;
 import org.springframework.security.access.prepost.PreAuthorize;
 import org.springframework.web.bind.annotation.*;
 import org.springframework.http.ResponseEntity;

 import jakarta.validation.Valid;
 import lombok.RequiredArgsConstructor;
 import java.util.List;
 import java.util.stream.Collectors;

 import org.springframework.http.HttpStatus;

 @RestController
 @RequiredArgsConstructor
 @RequestMapping("/cronograma")
 public class CronogramaController {
     private final Fachada fachada;
     private final ModelMapper modelMapper;


     @GetMapping
     public Page<CronogramaResponse> listar(@PageableDefault(sort = "id") Pageable pageable) {
         return fachada.listarCronogramas(pageable).map(cronograma -> new CronogramaResponse(cronograma, modelMapper));
     }

     @GetMapping("/{id}")
     public ResponseEntity<CronogramaResponse> buscar(@PathVariable Long id) throws CronogramaNotFoundException {
         Cronograma response = fachada.buscarCronograma(id);
         return new ResponseEntity<>(new CronogramaResponse(response, modelMapper), HttpStatus.OK);
     }

     @GetMapping("/tipo-atendimento/{id}")
        public ResponseEntity<Page<CronogramaResponse>> buscarPorTipoAtendimento(@PathVariable Long id, @PageableDefault(sort = "id") Pageable pageable) throws TipoAtendimentoNotFoundException{
            Page<CronogramaResponse> response = fachada.listarCronogramasPorTipoAtendimento(id, pageable).map(cronograma -> new CronogramaResponse(cronograma, modelMapper));
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

     @PreAuthorize("hasRole('PROFISSIONAL')")
     @PostMapping
     public ResponseEntity<List<CronogramaResponse>> salvar(@Valid @RequestBody CronogramaRequest entity) throws TipoAtendimentoNotFoundException {
         List<Cronograma> entities = entity.convertToEntities(modelMapper);

         List<CronogramaResponse> responses = entities.stream()
                 .map(cronogramaEntity -> {
                     Cronograma criado = fachada.salvarCronograma(
                             cronogramaEntity,
                             entity.getTipoAtendimentoId()
                     );
                     return new CronogramaResponse(criado, modelMapper);
                 })
                 .collect(Collectors.toList());

         return new ResponseEntity<>(responses, HttpStatus.CREATED);
     }

     @PreAuthorize("hasRole('PROFISSIONAL')")
     @GetMapping("/profissional")
     public Page<CronogramaResponse> listarPorProfissional(@PageableDefault(sort = "id") Pageable pageable) {
         return fachada.listarCronogramasPorProfissional(pageable).map(cronograma -> new CronogramaResponse(cronograma, modelMapper));
     }


     @PatchMapping("/{id}")
     public ResponseEntity<CronogramaResponse> editar(@PathVariable Long id, @Valid @RequestBody CronogramaUpdateRequest entity) throws CronogramaNotFoundException {
         Cronograma response = fachada.editarCronograma(id, entity.convertToEntity(entity, modelMapper), entity.getTipoAtendimentoId());
         return new ResponseEntity<>(new CronogramaResponse(response, modelMapper), HttpStatus.OK);
     }

     @DeleteMapping("/{id}")
     public ResponseEntity<Void> delete(@PathVariable Long id) {
         fachada.deletarCronograma(id);
         return new ResponseEntity<>(HttpStatus.NO_CONTENT);
     }
 }
