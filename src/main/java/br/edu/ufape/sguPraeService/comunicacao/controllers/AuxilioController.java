package br.edu.ufape.sguPraeService.comunicacao.controllers;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ufape.sguPraeService.comunicacao.dto.auxilio.AuxilioRequest;
import br.edu.ufape.sguPraeService.comunicacao.dto.auxilio.AuxilioResponse;
import br.edu.ufape.sguPraeService.comunicacao.dto.auxilio.RelatorioFinanceiroResponse;
import br.edu.ufape.sguPraeService.comunicacao.dto.documento.DocumentoResponse;
import br.edu.ufape.sguPraeService.exceptions.AuxilioNotFoundException;
import br.edu.ufape.sguPraeService.exceptions.TipoAuxilioNotFoundException;
import br.edu.ufape.sguPraeService.exceptions.TipoBolsaNotFoundException;
import br.edu.ufape.sguPraeService.exceptions.notFoundExceptions.EstudanteNotFoundException;
import br.edu.ufape.sguPraeService.fachada.Fachada;
import br.edu.ufape.sguPraeService.models.Auxilio;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auxilio")
public class AuxilioController {
    private final Fachada fachada;
    private final ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<Page<AuxilioResponse>> listar(@PageableDefault(sort = "id") Pageable pageable) {
        Page<AuxilioResponse> resposta = fachada.listarAuxilios(pageable)
                .map(auxilio -> new AuxilioResponse(auxilio, modelMapper));
        return ResponseEntity.ok(resposta);
    }

    @GetMapping("/list")
    public ResponseEntity<List<AuxilioResponse>> listar() {
        List<AuxilioResponse> resposta = fachada.listarAuxilios().stream()
                .map(auxilio -> new AuxilioResponse(auxilio, modelMapper)).toList();
        return ResponseEntity.ok(resposta);
    }

    @GetMapping("/estudante/{estudanteId}")
    public ResponseEntity<Page<AuxilioResponse>> listarPorEstudanteId(@PathVariable Long estudanteId,
            @PageableDefault(sort = "id") Pageable pageable)
            throws EstudanteNotFoundException {
        Page<AuxilioResponse> resposta = fachada.listarAuxiliosPorEstudanteId(estudanteId, pageable)
                .map(auxilio -> new AuxilioResponse(auxilio, modelMapper));
        return ResponseEntity.ok(resposta);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuxilioResponse> buscar(@PathVariable Long id) throws AuxilioNotFoundException {
        Auxilio response = fachada.buscarAuxilio(id);
        return new ResponseEntity<>(new AuxilioResponse(response, modelMapper), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('GESTOR') and hasRole('PRAE_ACCESS')")
    @GetMapping("/{id}/termo")
    public ResponseEntity<DocumentoResponse> buscarTermo(@PathVariable Long id)
            throws AuxilioNotFoundException, IOException {
        Auxilio auxilio = fachada.buscarAuxilio(id);
        DocumentoResponse termo = fachada.converterDocumentosParaBase64(List.of(auxilio.getTermo())).getFirst();
        return new ResponseEntity<>(termo, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('GESTOR') and hasRole('PRAE_ACCESS')")
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<AuxilioResponse> salvar(@Valid @ModelAttribute AuxilioRequest entity)
            throws TipoAuxilioNotFoundException, TipoBolsaNotFoundException {
        Auxilio response = entity.convertToEntity(entity, modelMapper);
        response = fachada.salvarAuxilio(entity.getEstudanteId(), response, entity.getTermo());
        return new ResponseEntity<>(new AuxilioResponse(response, modelMapper), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('GESTOR') and hasRole('PRAE_ACCESS')")
    @PatchMapping(value = "/{id}", consumes = "multipart/form-data")
    public ResponseEntity<AuxilioResponse> editar(@PathVariable Long id, @Valid @ModelAttribute AuxilioRequest entity)
            throws AuxilioNotFoundException, TipoAuxilioNotFoundException, TipoBolsaNotFoundException {
        Auxilio response = fachada.editarAuxilio(id, entity.convertToEntity(entity, modelMapper), entity.getTermo());
        return new ResponseEntity<>(new AuxilioResponse(response, modelMapper), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('GESTOR') and hasRole('PRAE_ACCESS')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) throws AuxilioNotFoundException {
        fachada.deletarAuxilio(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PreAuthorize("hasRole('GESTOR') and hasRole('PRAE_ACCESS')")
    @GetMapping("/pagos/{ano}/{mes}")
    public ResponseEntity<Page<AuxilioResponse>> listarPagosPorMes(@PathVariable int ano, @PathVariable int mes, @PageableDefault(sort = "id") Pageable pageable) throws AuxilioNotFoundException {
        Page<Auxilio> auxilios = fachada.listarPagosPorMes(ano, mes, pageable);
        return ResponseEntity.ok(
                auxilios.map(aux -> new AuxilioResponse(aux, modelMapper)));
    }

    @PreAuthorize("hasRole('GESTOR') and hasRole('PRAE_ACCESS')")
    @GetMapping("/tipo/{id}")
    public ResponseEntity<Page<AuxilioResponse>> listarPorTipo(@PathVariable Long id, @PageableDefault(sort = "id") Pageable pageable) throws AuxilioNotFoundException {
        Page<Auxilio> auxilios = fachada.listarAuxiliosPorTipo(id, pageable);
        return ResponseEntity.ok(
                auxilios.map(aux -> new AuxilioResponse(aux, modelMapper)));
    }

    @PreAuthorize("hasRole('GESTOR') and hasRole('PRAE_ACCESS')")
    @GetMapping("/pendentes")
    public ResponseEntity<Page<AuxilioResponse>> listarAuxiliosPendentesMesAtual(@PageableDefault(sort = "id") Pageable pageable) {
        Page<Auxilio> auxilios = fachada.listarAuxiliosPendentesMesAtual(pageable);
        return ResponseEntity.ok(
                auxilios.map(aux -> new AuxilioResponse(aux, modelMapper)));
    }

    @PreAuthorize("hasRole('GESTOR') and hasRole('PRAE_ACCESS')")
    @GetMapping("/relatorio/financeiro")
    public ResponseEntity<RelatorioFinanceiroResponse> gerarRelatorioFinanceiro(
            @RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate inicio,
            @RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate fim) {
        RelatorioFinanceiroResponse relatorio = fachada.gerarRelatorioFinanceiro(inicio, fim);
        return ResponseEntity.ok(relatorio);
    }
}
