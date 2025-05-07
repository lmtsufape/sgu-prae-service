package br.edu.ufape.sguPraeService.comunicacao.controllers;

import br.edu.ufape.sguPraeService.comunicacao.dto.estudante.*;
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

    @GetMapping("/curso/{id}")
    public List<EstudanteResponse> listarEstudantesPorCurso(@PathVariable Long id) {
        return fachada.listarEstudantesPorCurso(id);
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

    @GetMapping("/credores")
    public ResponseEntity<List<CredorResponse>> listarCredoresComAuxiliosAtivos() {
        List<CredorResponse> credores = fachada.listarCredoresComAuxiliosAtivos();
        return ResponseEntity.ok(credores);
    }

    @GetMapping("/credores/auxilio/{id}")
    public ResponseEntity<List<CredorResponse>> listarCredoresPorAuxilio(@PathVariable Long id) {
        List<CredorResponse> credores = fachada.listarCredoresPorAuxilio(id);
        return ResponseEntity.ok(credores);
    }

    @GetMapping("/credores/publicacao")
    public ResponseEntity<List<PublicacaoResponse>> listarCrediresParaPublicacao() {
        List<PublicacaoResponse> publicacoes = fachada.listarCredoresParaPublicacao().stream().map(alunoResponse -> new PublicacaoResponse(alunoResponse, modelMapper)).toList();
        return ResponseEntity.ok(publicacoes);
    }
    @GetMapping("/credores/curso/{id}")
    List<CredorResponse> listarCredoresPorCurso(@PathVariable Long id) {
        return fachada.listarCredoresPorCurso(id);
    }



    @GetMapping("/{id}/relatorio")
    public ResponseEntity<RelatorioEstudanteAssistidoResponse> gerarRelatorioAssistido(
            @PathVariable Long id
    ) throws EstudanteNotFoundException {
        RelatorioEstudanteAssistidoResponse relatorio = fachada.gerarRelatorioEstudanteAssistido(id);
        return ResponseEntity.ok(relatorio);
    }

}