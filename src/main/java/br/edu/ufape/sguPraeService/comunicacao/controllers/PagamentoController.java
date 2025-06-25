package br.edu.ufape.sguPraeService.comunicacao.controllers;

import br.edu.ufape.sguPraeService.comunicacao.dto.pagamento.PagamentoPatchRequest;
import br.edu.ufape.sguPraeService.fachada.Fachada;
import br.edu.ufape.sguPraeService.models.Pagamento;
import br.edu.ufape.sguPraeService.comunicacao.dto.pagamento.PagamentoResponse;
import br.edu.ufape.sguPraeService.comunicacao.dto.pagamento.PagamentoRequest;
import br.edu.ufape.sguPraeService.exceptions.AuxilioNotFoundException;
import br.edu.ufape.sguPraeService.exceptions.notFoundExceptions.PagamentoNotFoundException;

import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;

import br.edu.ufape.sguPraeService.models.Estudante;

@RestController
@RequiredArgsConstructor
@RequestMapping("/pagamento")
public class PagamentoController {
    private final Fachada fachada;
    private final ModelMapper modelMapper;

    @GetMapping("/list")
    public ResponseEntity<List<PagamentoResponse>> listar() {
        return ResponseEntity.ok(fachada.listarPagamentos().stream()
                .map(p -> new PagamentoResponse(p, getEstudantesPorPagamentoId(p.getId()), modelMapper)).toList());
    }

    @GetMapping
    public ResponseEntity<Page<PagamentoResponse>> listar(@PageableDefault(sort = "id") Pageable pageable) {
        return ResponseEntity.ok(fachada.listarPagamentos(pageable)
                .map(p -> new PagamentoResponse(p, getEstudantesPorPagamentoId(p.getId()), modelMapper)));
    }

    @GetMapping("/auxilio/{auxilioId}")
    public ResponseEntity<Page<PagamentoResponse>> listarPorAuxilioId(@PathVariable Long auxilioId, @PageableDefault(sort = "id") Pageable pageable) throws AuxilioNotFoundException {
        return ResponseEntity.ok(fachada.listarPagamentosPorAuxilioId(auxilioId, pageable)
                .map(p -> new PagamentoResponse(p, getEstudantesPorPagamentoId(p.getId()), modelMapper)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PagamentoResponse> buscar(@PathVariable Long id) throws PagamentoNotFoundException {
        Pagamento response = fachada.buscarPagamento(id);
        return new ResponseEntity<>(
                new PagamentoResponse(response, getEstudantesPorPagamentoId(response.getId()), modelMapper),
                HttpStatus.OK);
    }

    @PreAuthorize("hasRole('GESTOR') and hasRole('PRAE_ACCESS')")
    @PostMapping
    public ResponseEntity<List<PagamentoResponse>> salvar(@Valid @RequestBody List<PagamentoRequest> entities)
            throws AuxilioNotFoundException {
        if (entities.isEmpty())
            return ResponseEntity.badRequest().build();

        List<Pagamento> pagamentos = entities.stream()
                .map(e -> e.convertToEntity(e, modelMapper))
                .toList();

        List<Pagamento> salvos = fachada.salvarPagamentos(pagamentos, entities.getFirst().getAuxilioId());

        List<PagamentoResponse> response = salvos.stream()
                .map(p -> new PagamentoResponse(p, getEstudantesPorPagamentoId(p.getId()), modelMapper))
                .toList();

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('GESTOR') and hasRole('PRAE_ACCESS')")
    public ResponseEntity<PagamentoResponse> editar(@PathVariable Long id, @RequestBody PagamentoPatchRequest dto
    ) throws PagamentoNotFoundException {
        Pagamento atualizado = fachada.editarPagamento(id, dto);
        return ResponseEntity.ok(
                new PagamentoResponse(atualizado, getEstudantesPorPagamentoId(atualizado.getId()), modelMapper)
        );
    }


    @PreAuthorize("hasRole('GESTOR') and hasRole('PRAE_ACCESS')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id)
            throws PagamentoNotFoundException, AuxilioNotFoundException {
        fachada.deletarPagamento(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PreAuthorize("hasRole('GESTOR') and hasRole('PRAE_ACCESS')")
    @PatchMapping("desativar/{id}")
    public ResponseEntity<Void> desativar(@PathVariable Long id) throws PagamentoNotFoundException {
        fachada.desativarPagamento(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/valor/{min}/{max}")
    public ResponseEntity<List<PagamentoResponse>> listarPorValor(
            @PathVariable BigDecimal min,
            @PathVariable BigDecimal max) {
        return ResponseEntity.ok(fachada.listarPagamentosPorValor(min, max)
                .stream()
                .map(p -> new PagamentoResponse(p, getEstudantesPorPagamentoId(p.getId()), modelMapper))
                .toList());
    }

    @GetMapping("/estudante/{estudanteId}")
    public ResponseEntity<List<PagamentoResponse>> listarPorEstudante(@PathVariable Long estudanteId) {
        return ResponseEntity.ok(fachada.listarPagamentosPorEstudante(estudanteId).stream()
                .map(p -> new PagamentoResponse(p, getEstudantesPorPagamentoId(p.getId()), modelMapper))
                .toList());
    }

    private List<Estudante> getEstudantesPorPagamentoId(Long pagamentoId) {
        try {
            return fachada.buscarAuxilioPorPagamentoId(pagamentoId).getEstudantes();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}
