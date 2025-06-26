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

@RestController
@RequiredArgsConstructor
@RequestMapping("/beneficio")
public class BeneficioController {
    private final Fachada fachada;
    private final ModelMapper modelMapper;

    @GetMapping
    public Page<BeneficioResponse> listar(@PageableDefault(sort = "id") Pageable pageable) {
        Page<Beneficio> beneficios = fachada.listarBeneficios(pageable);
        return beneficios.map(beneficio -> new BeneficioResponse(beneficio, modelMapper));
    }

    @GetMapping("/estudante/{estudanteId}")
    public Page<BeneficioResponse> listarPorEstudanteId(@PageableDefault(sort = "id") Pageable pageable, @PathVariable Long estudanteId)
            throws EstudanteNotFoundException {
        Page<Beneficio> beneficios = fachada.listarBeneficiosPorEstudanteId(estudanteId,pageable);
        return beneficios.map(beneficio -> new BeneficioResponse(beneficio, modelMapper));

    }

    @GetMapping("/{id}")
    public ResponseEntity<BeneficioResponse> buscar(@PathVariable Long id) throws BeneficioNotFoundException {
        Beneficio response = fachada.buscarBeneficios(id);
        return new ResponseEntity<>(new BeneficioResponse(response, modelMapper), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('GESTOR') and hasRole('PRAE_ACCESS')")
    @GetMapping("/{id}/termo")
    public ResponseEntity<DocumentoResponse> buscarTermo(@PathVariable Long id)
            throws BeneficioNotFoundException, IOException {
        Beneficio beneficio = fachada.buscarBeneficios(id);
        DocumentoResponse termo = fachada.converterDocumentosParaBase64(List.of(beneficio.getTermo())).getFirst();
        return new ResponseEntity<>(termo, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('GESTOR') and hasRole('PRAE_ACCESS')")
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<BeneficioResponse> salvar(@Valid @ModelAttribute BeneficioRequest entity)
            throws TipoBeneficioNotFoundException {
        Beneficio response = entity.convertToEntity(entity, modelMapper);
        response = fachada.salvarBeneficios(entity.getEstudanteId(), response, entity.getTermo(), entity.getTipoBeneficioId());
        return new ResponseEntity<>(new BeneficioResponse(response, modelMapper), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('GESTOR') and hasRole('PRAE_ACCESS')")
    @PatchMapping(value = "/{id}", consumes = "multipart/form-data")
    public ResponseEntity<BeneficioResponse> editar(@PathVariable Long id, @Valid @ModelAttribute BeneficioRequest entity)
            throws BeneficioNotFoundException, TipoBeneficioNotFoundException{
        Beneficio response = fachada.editarBeneficios(id, entity.getEstudanteId(), entity.convertToEntity(entity, modelMapper), entity.getTermo(), entity.getTipoBeneficioId());
        return new ResponseEntity<>(new BeneficioResponse(response, modelMapper), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('GESTOR') and hasRole('PRAE_ACCESS')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) throws BeneficioNotFoundException {
        fachada.deletarBeneficio(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PreAuthorize("hasRole('GESTOR') and hasRole('PRAE_ACCESS')")
    @GetMapping("/pagos")
    public ResponseEntity<List<BeneficioResponse>> listarPagosPorMes() throws BeneficioNotFoundException {
        List<Beneficio> beneficios = fachada.listarPagosPorMes();
        return ResponseEntity.ok(
                beneficios.stream()
                        .map(aux -> new BeneficioResponse(aux, modelMapper))
                        .toList());
    }

    @PreAuthorize("hasRole('GESTOR') and hasRole('PRAE_ACCESS')")
    @GetMapping("/tipo/{id}")
    public ResponseEntity<Page<BeneficioResponse>> listarPorTipo(@PageableDefault(sort = "id") Pageable pageable, @PathVariable Long id) throws BeneficioNotFoundException {
        Page<Beneficio> beneficios = fachada.listarBeneficiosPorTipo(id, pageable);
        return ResponseEntity.ok(
                beneficios
                        .map(aux -> new BeneficioResponse(aux, modelMapper)));
    }

    @PreAuthorize("hasRole('GESTOR') and hasRole('PRAE_ACCESS')")
    @GetMapping("/pendentes")
    public ResponseEntity<List<BeneficioResponse>> listarAuxiliosPendentesMesAtual() {
        List<Beneficio> beneficios = fachada.listarBeneficiosPendentesMesAtual();
        return ResponseEntity.ok(
                beneficios.stream()
                        .map(aux -> new BeneficioResponse(aux, modelMapper))
                        .toList());
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
