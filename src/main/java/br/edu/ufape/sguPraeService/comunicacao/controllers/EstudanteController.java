package br.edu.ufape.sguPraeService.comunicacao.controllers;

import br.edu.ufape.sguPraeService.comunicacao.dto.estudante.EstudanteRequest;
import br.edu.ufape.sguPraeService.comunicacao.dto.estudante.EstudanteResponse;
import br.edu.ufape.sguPraeService.exceptions.notFoundExceptions.EstudanteNotFoundException;
import br.edu.ufape.sguPraeService.exceptions.notFoundExceptions.TipoEtniaNotFoundException;
import br.edu.ufape.sguPraeService.fachada.Fachada;
import br.edu.ufape.sguPraeService.models.Estudante;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/estudantes")
@RequiredArgsConstructor
public class EstudanteController {
    private final Fachada fachada;
    private final ModelMapper modelMapper;

    @GetMapping
    public List<EstudanteResponse> listarEstudantes() {
        return fachada.listarEstudantes();
    }

    @GetMapping("/{id}")
    public ResponseEntity<EstudanteResponse> buscarEstudante(@PathVariable Long id) throws EstudanteNotFoundException {
        return ResponseEntity.ok(fachada.buscarEstudante(id));
    }

    @PreAuthorize("hasRole('ALUNO')")
    @PostMapping
    public ResponseEntity<EstudanteResponse> criarEstudante(@Valid @RequestBody EstudanteRequest estudanteRequest) throws TipoEtniaNotFoundException {
        Estudante estudante = estudanteRequest.convertToEntity(estudanteRequest, modelMapper);
        EstudanteResponse novoEstudante = fachada.salvarEstudante(estudante, estudanteRequest.getTipoEtniaId());
        return ResponseEntity.status(HttpStatus.CREATED).body(novoEstudante);
    }

    @PreAuthorize("hasRole('ESTUDANTE')")
    @PatchMapping
    public ResponseEntity<EstudanteResponse> atualizarEstudante(@Valid @RequestBody EstudanteRequest estudanteRequest) throws EstudanteNotFoundException, TipoEtniaNotFoundException {
        Estudante estudante = estudanteRequest.convertToEntity(estudanteRequest, modelMapper);
        EstudanteResponse estudanteAtualizado = fachada.atualizarEstudante(estudante,estudanteRequest.getTipoEtniaId());

        return ResponseEntity.ok(estudanteAtualizado);
    }

    @DeleteMapping("/{id}/")
    public ResponseEntity<Void> deletarEstudante(@PathVariable Long id) throws EstudanteNotFoundException{
        fachada.deletarEstudante(id);
        return ResponseEntity.noContent().build();
    }
}