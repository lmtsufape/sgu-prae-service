package br.edu.ufape.sguPraeService.comunicacao.controllers;

import br.edu.ufape.sguPraeService.comunicacao.dto.agendamento.AgendamentoResponse;
import br.edu.ufape.sguPraeService.comunicacao.dto.cancelamentoAgendamento.CancelamentoRequest;
import br.edu.ufape.sguPraeService.comunicacao.dto.cancelamentoAgendamento.CancelamentoResponse;
import br.edu.ufape.sguPraeService.fachada.Fachada;
import br.edu.ufape.sguPraeService.models.CancelamentoAgendamento;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/agendamento")
public class AgendamentoController {
    private final Fachada fachada;
    private final ModelMapper modelMapper;


    @PreAuthorize("hasRole('ESTUDANTE')")
    @PostMapping("/{vagaId}/agendar")
    public ResponseEntity<AgendamentoResponse> agendarVaga(@PathVariable Long vagaId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(new AgendamentoResponse(fachada.agendarVaga(vagaId), modelMapper));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AgendamentoResponse> buscarAgendamento(@PathVariable Long id) {
        return ResponseEntity.ok(new AgendamentoResponse(fachada.buscarAgendamento(id), modelMapper));
    }

    @GetMapping("/estudante")
    public List<AgendamentoResponse> listarAgendamentosPorEstudanteAtual() {
        return fachada.listarAgendamentoPorEstudanteAtual().stream().map(agendamento -> new AgendamentoResponse(agendamento, modelMapper)).toList();
    }

    @GetMapping("/estudante/{estudanteId}")
    public List<AgendamentoResponse> listarAgendamentosPorEstudante(@PathVariable Long estudanteId) {
        return fachada.listarAgendamentosPorEstudante(estudanteId).stream().map(agendamento -> new AgendamentoResponse(agendamento, modelMapper)).toList();
    }

    @GetMapping("/profissional")
    public List<AgendamentoResponse> listarAgendamentosPorProfissionalAtual() {
        return fachada.listarAgendamentoPorProfissionalAtual().stream().map(agendamento -> new AgendamentoResponse(agendamento, modelMapper)).toList();
    }

    @GetMapping("/profissional/{profissionalId}")
    public List<AgendamentoResponse> listarAgendamentosPorProfissional(@PathVariable Long profissionalId) {
        return fachada.listarAgendamentosPorProfissional(profissionalId).stream().map(agendamento -> new AgendamentoResponse(agendamento, modelMapper)).toList();
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
    public List<CancelamentoResponse> listarCancelamentosPorEstudanteAtual() {
        return fachada.listarCancelamentosPorEstudanteAtual().stream().map(cancelamento -> new CancelamentoResponse(cancelamento, modelMapper)).toList();
    }

    @GetMapping("/cancelamento/profissional")
    public List<CancelamentoResponse> listarCancelamentosPorProfissionalAtual() {
        return fachada.listarCancelamentosPorProfissionalAtual().stream().map(cancelamento -> new CancelamentoResponse(cancelamento, modelMapper)).toList();
    }
}
