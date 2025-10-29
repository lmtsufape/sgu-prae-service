package br.edu.ufape.sguPraeService.comunicacao.controllers;

import br.edu.ufape.sguPraeService.comunicacao.dto.pagamento.PagamentoPatchRequest;
import br.edu.ufape.sguPraeService.comunicacao.dto.pagamento.PagamentoRequest;
import br.edu.ufape.sguPraeService.comunicacao.dto.pagamento.PagamentoResponse;
import br.edu.ufape.sguPraeService.exceptions.BeneficioNotFoundException;
import br.edu.ufape.sguPraeService.exceptions.notFoundExceptions.PagamentoNotFoundException;
import br.edu.ufape.sguPraeService.fachada.Fachada;
import br.edu.ufape.sguPraeService.models.Pagamento;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.querydsl.core.types.Predicate;
import org.springframework.data.querydsl.binding.QuerydslPredicate;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/pagamento")
public class PagamentoController {
    private final Fachada fachada;
    private final ModelMapper modelMapper;

    @GetMapping
    public Page<PagamentoResponse> listar(
            @QuerydslPredicate(root = Pagamento.class) Predicate predicate,
            @PageableDefault(sort = "id") Pageable pageable) {
        return fachada.listarPagamentos(predicate, pageable)
                .map(fachada::mapToPagamentoResponse);
    }

    @GetMapping("/beneficio/{beneficioId}")
    public List<PagamentoResponse> listarPorBeneficioId(@PathVariable Long beneficioId) throws BeneficioNotFoundException {
        return fachada.listarPagamentosPorBeneficioId(beneficioId)
                .stream()
                .map(fachada::mapToPagamentoResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PagamentoResponse> buscar(@PathVariable Long id) throws PagamentoNotFoundException {
        Pagamento pagamento = fachada.buscarPagamento(id);
        return ResponseEntity.ok(fachada.mapToPagamentoResponse(pagamento));
    }

    @PreAuthorize("hasRole('GESTOR') and hasRole('PRAE_ACCESS')")
    @PostMapping
    public ResponseEntity<List<PagamentoResponse>> salvar(@Valid @RequestBody List<PagamentoRequest> entities)
            throws BeneficioNotFoundException {
        if (entities.isEmpty()) return ResponseEntity.badRequest().build();

        List<Pagamento> pagamentos = entities.stream()
                .map(e -> e.convertToEntity(e, modelMapper))
                .toList();

        List<Pagamento> salvos = fachada.salvarPagamentos(pagamentos);

        List<PagamentoResponse> response = salvos.stream()
                .map(fachada::mapToPagamentoResponse)
                .toList();

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('GESTOR') and hasRole('PRAE_ACCESS')")
    public ResponseEntity<PagamentoResponse> editar(@PathVariable Long id, @RequestBody PagamentoPatchRequest dto)
            throws PagamentoNotFoundException {
        Pagamento atualizado = fachada.editarPagamento(id, dto);
        return ResponseEntity.ok(fachada.mapToPagamentoResponse(atualizado));
    }

    @PreAuthorize("hasRole('GESTOR') and hasRole('PRAE_ACCESS')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) throws PagamentoNotFoundException {
        fachada.deletarPagamento(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('GESTOR') and hasRole('PRAE_ACCESS')")
    @PatchMapping("desativar/{id}")
    public ResponseEntity<Void> desativar(@PathVariable Long id) throws PagamentoNotFoundException {
        fachada.desativarPagamento(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/valor/{min}/{max}")
    public Page<PagamentoResponse> listarPorValor(@PageableDefault(sort = "id") Pageable pageable, @PathVariable BigDecimal min, @PathVariable BigDecimal max) {
        return fachada.listarPagamentosPorValor(min, max, pageable)
                .map(fachada::mapToPagamentoResponse);
    }

    @GetMapping("/estudante/{estudanteId}")
    public List<PagamentoResponse> listarPorEstudante(@PathVariable Long estudanteId) {
        return fachada.listarPagamentosPorEstudante(estudanteId).stream()
                .map(fachada::mapToPagamentoResponse)
                .toList();
    }
}
