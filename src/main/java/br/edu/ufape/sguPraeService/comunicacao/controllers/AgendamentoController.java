package br.edu.ufape.sguPraeService.comunicacao.controllers;

import br.edu.ufape.sguPraeService.comunicacao.dto.agendamento.AgendamentoRequest;
import br.edu.ufape.sguPraeService.comunicacao.dto.agendamento.AgendamentoResponse;
import br.edu.ufape.sguPraeService.comunicacao.dto.cancelamentoAgendamento.CancelamentoRequest;
import br.edu.ufape.sguPraeService.comunicacao.dto.cancelamentoAgendamento.CancelamentoResponse;
import br.edu.ufape.sguPraeService.fachada.Fachada;
import br.edu.ufape.sguPraeService.models.CancelamentoAgendamento;
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


@RestController
@RequiredArgsConstructor
@RequestMapping("/agendamento")
public class AgendamentoController {
    private final Fachada fachada;
    private final ModelMapper modelMapper;


    @PreAuthorize("hasRole('ESTUDANTE')")
    @PostMapping("/agendar")
    public ResponseEntity<AgendamentoResponse> agendarVaga(@Valid @RequestBody AgendamentoRequest request) {
        AgendamentoResponse response = fachada.agendarVaga(request.getVagaId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AgendamentoResponse> buscarAgendamento(@PathVariable Long id) {
        return ResponseEntity.ok(fachada.buscarAgendamento(id));
    }

    @GetMapping("/estudante")
    public Page<AgendamentoResponse> listarAgendamentosPorEstudanteAtual(@PageableDefault(sort = "id") Pageable pageable) {
        return fachada.listarAgendamentoPorEstudanteAtual(pageable);
    }

    @GetMapping("/estudante/{estudanteId}")
    public Page<AgendamentoResponse> listarAgendamentosPorEstudante(@PathVariable Long estudanteId, @PageableDefault(sort = "id") Pageable pageable) {
        return fachada.listarAgendamentosPorEstudante(estudanteId, pageable);
    }

    @GetMapping("/profissional")
    public Page<AgendamentoResponse> listarAgendamentosPorProfissionalAtual(@PageableDefault(sort = "id") Pageable pageable) {
        return fachada.listarAgendamentoPorProfissionalAtual(pageable);
    }

    @GetMapping("/profissional/{profissionalId}")
    public Page<AgendamentoResponse> listarAgendamentosPorProfissional(@PathVariable Long profissionalId, @PageableDefault(sort = "id") Pageable pageable) {
        return fachada.listarAgendamentosPorProfissional(profissionalId, pageable);
    }

    @PostMapping( "{id}/cancelar")
    public ResponseEntity<CancelamentoResponse> cancelarAgendamento(@PathVariable Long id, @Valid @RequestBody CancelamentoRequest cancelamentoRequest) {
        CancelamentoAgendamento cancelamento = cancelamentoRequest.convertToEntity(cancelamentoRequest, modelMapper);
        return ResponseEntity.ok(new CancelamentoResponse(fachada.cancelarAgendamento(id, cancelamento), modelMapper));
    }

    @GetMapping("/cancelamento/{id}")
    public ResponseEntity<CancelamentoResponse> buscarCancelamentoAgendamento(@PathVariable Long id) {
        return ResponseEntity.ok(new CancelamentoResponse(fachada.buscarCancelamento(id), modelMapper));
    }

    @GetMapping("/cancelamento/estudante")
    public Page<CancelamentoResponse> listarCancelamentosPorEstudanteAtual(@PageableDefault(sort = "id") Pageable pageable) {
        return fachada.listarCancelamentosPorEstudanteAtual(pageable).map(cancelamento -> new CancelamentoResponse(cancelamento, modelMapper));
    }

    @GetMapping("/cancelamento/profissional")
    public Page<CancelamentoResponse> listarCancelamentosPorProfissionalAtual(@PageableDefault(sort = "id") Pageable pageable) {
        return fachada.listarCancelamentosPorProfissionalAtual(pageable).map(cancelamento -> new CancelamentoResponse(cancelamento, modelMapper));
    }
}