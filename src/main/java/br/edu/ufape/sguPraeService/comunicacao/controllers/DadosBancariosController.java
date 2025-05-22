package br.edu.ufape.sguPraeService.comunicacao.controllers;

import br.edu.ufape.sguPraeService.comunicacao.dto.dadosBancarios.DadosBancariosRequest;
import br.edu.ufape.sguPraeService.comunicacao.dto.dadosBancarios.DadosBancariosResponse;
import br.edu.ufape.sguPraeService.fachada.Fachada;
import br.edu.ufape.sguPraeService.models.DadosBancarios;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/dadosBancarios")
@RequiredArgsConstructor
public class DadosBancariosController {
    private final Fachada fachada;
    private final ModelMapper modelMapper;

    @GetMapping("/{id}")
    public ResponseEntity<DadosBancariosResponse> buscarDadosBancarios(@PathVariable Long id) {
        DadosBancarios dadosBancarios = fachada.buscarDadosBancarios(id);
        if (dadosBancarios == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new DadosBancariosResponse(dadosBancarios, modelMapper), HttpStatus.OK);
    }

    @GetMapping
    public List<DadosBancariosResponse> listarDadosBancarios() {
        return fachada.listarDadosBancarios().stream()
                .map(dadosBancarios -> new DadosBancariosResponse(dadosBancarios, modelMapper))
                .collect(Collectors.toList());
    }

    @PreAuthorize("hasRole('GESTOR') and hasRole('PRAE_ACCESS')")
    @PostMapping("/{idEstudante}")
    public ResponseEntity<DadosBancariosResponse> criarDadosBancarios(@PathVariable Long idEstudante, @RequestBody DadosBancariosRequest dadosBancariosRequest) {
        DadosBancarios dadosBancarios = dadosBancariosRequest.convertToEntity(dadosBancariosRequest, modelMapper);
        DadosBancarios novoDadosBancarios = fachada.salvarDadosBancarios(idEstudante, dadosBancarios);
        return new ResponseEntity<>(new DadosBancariosResponse(novoDadosBancarios, modelMapper), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('GESTOR') and hasRole('PRAE_ACCESS')")
    @PatchMapping("/{id}")
    public ResponseEntity<DadosBancariosResponse> atualizarDadosBancarios(@PathVariable Long id, @RequestBody DadosBancariosRequest dadosBancariosRequest) {
        DadosBancarios dadosBancarios = dadosBancariosRequest.convertToEntity(dadosBancariosRequest, modelMapper);
        DadosBancarios dadosBancariosAtualizados = fachada.atualizarDadosBancarios(id, dadosBancarios);
        if (dadosBancariosAtualizados == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new DadosBancariosResponse(dadosBancariosAtualizados, modelMapper), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('GESTOR') and hasRole('PRAE_ACCESS')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarDadosBancarios(@PathVariable Long id) {
        fachada.deletarDadosBancarios(id);
        return ResponseEntity.noContent().build();
    }
}
