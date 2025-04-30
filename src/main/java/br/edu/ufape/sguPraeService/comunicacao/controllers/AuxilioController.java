package br.edu.ufape.sguPraeService.comunicacao.controllers;
import br.edu.ufape.sguPraeService.comunicacao.dto.auxilio.RelatorioFinanceiroResponse;
import br.edu.ufape.sguPraeService.fachada.Fachada;
import br.edu.ufape.sguPraeService.models.Auxilio;
import br.edu.ufape.sguPraeService.comunicacao.dto.auxilio.AuxilioResponse;
import br.edu.ufape.sguPraeService.comunicacao.dto.auxilio.AuxilioRequest;
import br.edu.ufape.sguPraeService.exceptions.AuxilioNotFoundException;
import br.edu.ufape.sguPraeService.exceptions.TipoAuxilioNotFoundException;
import br.edu.ufape.sguPraeService.exceptions.TipoBolsaNotFoundException;
import br.edu.ufape.sguPraeService.exceptions.notFoundExceptions.EstudanteNotFoundException;

import org.modelmapper.ModelMapper;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
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
    
    @GetMapping("/estudante/{estudanteId}")
    public List<AuxilioResponse> listarPorEstudanteId(@PathVariable Long estudanteId) throws EstudanteNotFoundException{
        return fachada.listarAuxilios().stream().map(auxilio -> new AuxilioResponse(auxilio, modelMapper)).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuxilioResponse> buscar(@PathVariable Long id) throws AuxilioNotFoundException {
        Auxilio response = fachada.buscarAuxilio(id);
        return new ResponseEntity<>(new AuxilioResponse(response, modelMapper), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<AuxilioResponse> salvar(@Valid @RequestBody AuxilioRequest entity) throws TipoAuxilioNotFoundException, TipoBolsaNotFoundException {
    	Auxilio auxilio = entity.convertToEntity(entity, modelMapper);
        auxilio = fachada.salvarAuxilio(auxilio, entity.getEstudanteId());
        return new ResponseEntity<>(new AuxilioResponse(auxilio, modelMapper), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<AuxilioResponse> editar(@PathVariable Long id, @Valid @RequestBody AuxilioRequest entity) throws AuxilioNotFoundException, TipoAuxilioNotFoundException, TipoBolsaNotFoundException {
        Auxilio response = fachada.editarAuxilio(id, entity.convertToEntity(entity, modelMapper));
        return new ResponseEntity<>(new AuxilioResponse(response, modelMapper), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) throws AuxilioNotFoundException {
        fachada.deletarAuxilio(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/relatorio/financeiro")
    public ResponseEntity<RelatorioFinanceiroResponse> gerarRelatorioFinanceiro(
            @RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate inicio,
            @RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate fim) {
        RelatorioFinanceiroResponse relatorio = fachada.gerarRelatorioFinanceiro(inicio, fim);
        return ResponseEntity.ok(relatorio);
    }
}
