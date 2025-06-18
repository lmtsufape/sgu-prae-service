package br.edu.ufape.sguPraeService.comunicacao.controllers;
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
import java.util.List;
import org.springframework.http.HttpStatus;

@RestController
@RequiredArgsConstructor
@RequestMapping("/pagamento")
public class PagamentoController {
    private final Fachada fachada;
    private final ModelMapper modelMapper;


    @GetMapping
    public List<PagamentoResponse> listar() {
        return fachada.listarPagamentos().stream().map(pagamento -> new PagamentoResponse(pagamento, modelMapper)).toList();
    }
    
    @GetMapping("/auxilio/{auxilioId}")
    public List<PagamentoResponse> listarPorAuxilioId(@PathVariable Long auxilioId) throws AuxilioNotFoundException {
        return fachada.listarPagamentosPorAuxilioId(auxilioId).stream().map(pagamento -> new PagamentoResponse(pagamento, modelMapper)).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PagamentoResponse> buscar(@PathVariable Long id) throws PagamentoNotFoundException {
        Pagamento response = fachada.buscarPagamento(id);
        return new ResponseEntity<>(new PagamentoResponse(response, modelMapper), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('GESTOR') and hasRole('PRAE_ACCESS')")
    @PostMapping
    public ResponseEntity<List<PagamentoResponse>> salvar(@Valid @RequestBody List<PagamentoRequest> entities) throws AuxilioNotFoundException {
        if (entities.isEmpty()) return ResponseEntity.badRequest().build();

        List<Pagamento> pagamentos = entities.stream()
                .map(e -> e.convertToEntity(e, modelMapper))
                .toList();

        List<Pagamento> salvos = fachada.salvarPagamentos(pagamentos, entities.getFirst().getAuxilioId());

        List<PagamentoResponse> response = salvos.stream()
                .map(p -> new PagamentoResponse(p, modelMapper))
                .toList();

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('GESTOR') and hasRole('PRAE_ACCESS')")
    @PatchMapping("/{id}")
    public ResponseEntity<PagamentoResponse> editar(@PathVariable Long id, @Valid @RequestBody PagamentoRequest entity) throws PagamentoNotFoundException {
        Pagamento response = fachada.editarPagamento(id, entity.convertToEntity(entity, modelMapper));
        return new ResponseEntity<>(new PagamentoResponse(response, modelMapper), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('GESTOR') and hasRole('PRAE_ACCESS')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) throws PagamentoNotFoundException {
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
    public List<PagamentoResponse> listarPorValor(
            @PathVariable BigDecimal min,
            @PathVariable BigDecimal max) {
        return fachada.listarPagamentosPorValor(min, max)
                .stream()
                .map(p -> new PagamentoResponse(p, modelMapper))
                .toList();
    }

    @GetMapping("/estudante/{estudanteId}")
    public List<PagamentoResponse> listarPorEstudante(@PathVariable Long estudanteId) {
        return fachada.listarPagamentosPorEstudante(estudanteId).stream()
                .map(p -> new PagamentoResponse(p, modelMapper))
                .toList();
    }
}

