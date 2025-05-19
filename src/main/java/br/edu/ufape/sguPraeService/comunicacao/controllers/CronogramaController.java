 package br.edu.ufape.sguPraeService.comunicacao.controllers;

 import br.edu.ufape.sguPraeService.exceptions.notFoundExceptions.TipoAtendimentoNotFoundException;
 import br.edu.ufape.sguPraeService.fachada.Fachada;
 import br.edu.ufape.sguPraeService.models.Cronograma;
 import br.edu.ufape.sguPraeService.comunicacao.dto.cronograma.CronogramaResponse;
 import br.edu.ufape.sguPraeService.comunicacao.dto.cronograma.CronogramaRequest;
 import br.edu.ufape.sguPraeService.exceptions.notFoundExceptions.CronogramaNotFoundException;

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
 @RequestMapping("/cronograma")
 public class CronogramaController {
     private final Fachada fachada;
     private final ModelMapper modelMapper;


     @GetMapping
     public List<CronogramaResponse> listar() {
         return fachada.listarCronogramas().stream().map(cronograma -> new CronogramaResponse(cronograma, modelMapper)).toList();
     }

     @GetMapping("/{id}")
     public ResponseEntity<CronogramaResponse> buscar(@PathVariable Long id) throws CronogramaNotFoundException {
         Cronograma response = fachada.buscarCronograma(id);
         return new ResponseEntity<>(new CronogramaResponse(response, modelMapper), HttpStatus.OK);
     }

     @GetMapping("/tipo-atendimento/{id}")
        public ResponseEntity<List<CronogramaResponse>> buscarPorTipoAtendimento(@PathVariable Long id) {
            List<CronogramaResponse> response = fachada.listarCronogramasPorTipoAtendimento(id).stream().map(cronograma -> new CronogramaResponse(cronograma, modelMapper)).toList();
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

     @PreAuthorize("hasRole('PROFISSIONAL')")
     @PostMapping
     public ResponseEntity<CronogramaResponse> salvar(@Valid @RequestBody CronogramaRequest entity) throws TipoAtendimentoNotFoundException {
         Cronograma response = fachada.salvarCronograma(entity.convertToEntity(entity, modelMapper), entity.getTipoAtendimentoId());
         return new ResponseEntity<>(new CronogramaResponse(response, modelMapper), HttpStatus.CREATED);
     }

     @PreAuthorize("hasRole('PROFISSIONAL')")
     @GetMapping("/profissional")
     public List<CronogramaResponse> listarPorProfissional() {
         return fachada.listarCronogramasPorProfissional().stream().map(cronograma -> new CronogramaResponse(cronograma, modelMapper)).toList();
     }


     @PatchMapping("/{id}")
     public ResponseEntity<CronogramaResponse> editar(@PathVariable Long id, @Valid @RequestBody CronogramaRequest entity) throws CronogramaNotFoundException {
         Cronograma response = fachada.editarCronograma(id, entity.convertToEntity(entity, modelMapper), entity.getTipoAtendimentoId());
         return new ResponseEntity<>(new CronogramaResponse(response, modelMapper), HttpStatus.OK);
     }

     @DeleteMapping("/{id}")
     public ResponseEntity<Void> delete(@PathVariable Long id) {
         fachada.deletarCronograma(id);
         return new ResponseEntity<>(HttpStatus.NO_CONTENT);
     }
 }
