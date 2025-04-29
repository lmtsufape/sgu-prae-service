package br.edu.ufape.sguPraeService.comunicacao.controllers;
import br.edu.ufape.sguPraeService.fachada.Fachada;
import br.edu.ufape.sguPraeService.models.Pagamento;
import br.edu.ufape.sguPraeService.comunicacao.dto.pagamento.PagamentoResponse;
import br.edu.ufape.sguPraeService.comunicacao.dto.pagamento.PagamentoRequest;
import br.edu.ufape.sguPraeService.exceptions.AuxilioNotFoundException;
import br.edu.ufape.sguPraeService.exceptions.notFoundExceptions.PagamentoNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

    @PostMapping
    public ResponseEntity<PagamentoResponse> salvar(@Valid @RequestBody PagamentoRequest entity) throws AuxilioNotFoundException {
        Pagamento response = fachada.salvarPagamento(entity.getAuxilioId(), entity.convertToEntity(entity, modelMapper));
        return new ResponseEntity<>(new PagamentoResponse(response, modelMapper), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PagamentoResponse> editar(@PathVariable Long id, @Valid @RequestBody PagamentoRequest entity) throws PagamentoNotFoundException {
        Pagamento response = fachada.editarPagamento(id, entity.convertToEntity(entity, modelMapper));
        return new ResponseEntity<>(new PagamentoResponse(response, modelMapper), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) throws PagamentoNotFoundException {
        fachada.deletarPagamento(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

