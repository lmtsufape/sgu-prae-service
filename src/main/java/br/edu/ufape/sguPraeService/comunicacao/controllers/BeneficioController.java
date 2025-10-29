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
import org.springframework.web.bind.annotation.*;

import br.edu.ufape.sguPraeService.comunicacao.dto.beneficio.BeneficioRequest;
import br.edu.ufape.sguPraeService.comunicacao.dto.beneficio.BeneficioResponse;
import br.edu.ufape.sguPraeService.comunicacao.dto.beneficio.RelatorioFinanceiroResponse;
import br.edu.ufape.sguPraeService.comunicacao.dto.documento.DocumentoResponse;
import br.edu.ufape.sguPraeService.exceptions.BeneficioNotFoundException;
import br.edu.ufape.sguPraeService.exceptions.TipoBeneficioNotFoundException;
import br.edu.ufape.sguPraeService.exceptions.notFoundExceptions.EstudanteNotFoundException;
import br.edu.ufape.sguPraeService.fachada.Fachada;
import br.edu.ufape.sguPraeService.models.Beneficio;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import com.querydsl.core.types.Predicate;
import org.springframework.data.querydsl.binding.QuerydslPredicate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/beneficio")
public class BeneficioController {
    private final Fachada fachada;
    private final ModelMapper modelMapper;

    @GetMapping
    public Page<BeneficioResponse> listar(
            @QuerydslPredicate(root = Beneficio.class) Predicate predicate,
            @PageableDefault(sort = "id") Pageable pageable) {
        return fachada.listarBeneficios(predicate, pageable)
                .map(fachada::mapToBeneficioResponse);
    }

    @GetMapping("/estudante/{estudanteId}")
    public Page<BeneficioResponse> listarPorEstudanteId(@PageableDefault(sort = "id") Pageable pageable, @PathVariable Long estudanteId)
            throws EstudanteNotFoundException {
        return fachada.listarBeneficiosPorEstudanteId(estudanteId, pageable)
                .map(fachada::mapToBeneficioResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BeneficioResponse> buscar(@PathVariable Long id) throws BeneficioNotFoundException {
        Beneficio beneficio = fachada.buscarBeneficios(id);
        return ResponseEntity.ok(fachada.mapToBeneficioResponse(beneficio));
    }

    @PreAuthorize("hasRole('GESTOR') and hasRole('PRAE_ACCESS')")
    @GetMapping("/{id}/termo")
    public ResponseEntity<DocumentoResponse> buscarTermo(@PathVariable Long id)
            throws BeneficioNotFoundException, IOException {
        Beneficio beneficio = fachada.buscarBeneficios(id);
        DocumentoResponse termo = fachada.converterDocumentosParaBase64(List.of(beneficio.getTermo())).getFirst();
        return ResponseEntity.ok(termo);
    }

    @PreAuthorize("hasRole('GESTOR') and hasRole('PRAE_ACCESS')")
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<BeneficioResponse> salvar(@Valid @ModelAttribute BeneficioRequest entity)
            throws TipoBeneficioNotFoundException {
        Beneficio beneficio = entity.convertToEntity(entity, modelMapper);
        beneficio = fachada.salvarBeneficios(entity.getEstudanteId(), beneficio, entity.getTermo(), entity.getTipoBeneficioId());
        return new ResponseEntity<>(fachada.mapToBeneficioResponse(beneficio), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('GESTOR') and hasRole('PRAE_ACCESS')")
    @PatchMapping(value = "/{id}", consumes = "multipart/form-data")
    public ResponseEntity<BeneficioResponse> editar(@PathVariable Long id, @Valid @ModelAttribute BeneficioRequest entity)
            throws BeneficioNotFoundException, TipoBeneficioNotFoundException {
        Beneficio beneficio = entity.convertToEntity(entity, modelMapper);
        beneficio = fachada.editarBeneficios(id, entity.getEstudanteId(), beneficio, entity.getTermo(), entity.getTipoBeneficioId());
        return ResponseEntity.ok(fachada.mapToBeneficioResponse(beneficio));
    }

    @PreAuthorize("hasRole('GESTOR') and hasRole('PRAE_ACCESS')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) throws BeneficioNotFoundException {
        fachada.deletarBeneficio(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('GESTOR') and hasRole('PRAE_ACCESS')")
    @GetMapping("/pagos")
    public ResponseEntity<List<BeneficioResponse>> listarPagosPorMes() throws BeneficioNotFoundException {
        List<Beneficio> beneficios = fachada.listarPagosPorMes();
        return ResponseEntity.ok(
                beneficios.stream()
                        .map(fachada::mapToBeneficioResponse)
                        .toList()
        );
    }

    @PreAuthorize("hasRole('GESTOR') and hasRole('PRAE_ACCESS')")
    @GetMapping("/tipo/{id}")
    public ResponseEntity<Page<BeneficioResponse>> listarPorTipo(@PageableDefault(sort = "id") Pageable pageable, @PathVariable Long id)
            throws BeneficioNotFoundException {
        return ResponseEntity.ok(
                fachada.listarBeneficiosPorTipo(id, pageable)
                        .map(fachada::mapToBeneficioResponse)
        );
    }

    @PreAuthorize("hasRole('GESTOR') and hasRole('PRAE_ACCESS')")
    @GetMapping("/pendentes")
    public ResponseEntity<List<BeneficioResponse>> listarAuxiliosPendentesMesAtual() {
        List<Beneficio> beneficios = fachada.listarBeneficiosPendentesMesAtual();
        return ResponseEntity.ok(
                beneficios.stream()
                        .map(fachada::mapToBeneficioResponse)
                        .toList()
        );
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
