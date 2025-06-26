package br.edu.ufape.sguPraeService.comunicacao.controllers;


import java.util.UUID;
import br.edu.ufape.sguPraeService.comunicacao.dto.estudante.*;
import br.edu.ufape.sguPraeService.comunicacao.dto.usuario.AlunoResponse;
import br.edu.ufape.sguPraeService.exceptions.notFoundExceptions.EstudanteNotFoundException;
import br.edu.ufape.sguPraeService.exceptions.notFoundExceptions.TipoEtniaNotFoundException;
import br.edu.ufape.sguPraeService.fachada.Fachada;
import br.edu.ufape.sguPraeService.models.Estudante;
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
@RequestMapping("/estudantes")
@RequiredArgsConstructor
public class EstudanteController {
    private final Fachada fachada;
    private final ModelMapper modelMapper;

    @GetMapping
    public Page<EstudanteResponse> listarEstudantes(@PageableDefault(sort = "id") Pageable pageable) {
        return fachada.listarEstudantes(pageable);
    }

    @GetMapping("/curso/{id}")
    public Page<EstudanteResponse> listarEstudantesPorCurso(
            @PathVariable Long id,
            @PageableDefault(sort = "id") Pageable pageable
    ) {
        return fachada.listarEstudantesPorCurso(id, pageable);
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
    public ResponseEntity<EstudanteResponse> atualizarEstudante(@Valid @RequestBody EstudanteUpdateRequest estudanteUpdateRequest) throws EstudanteNotFoundException, TipoEtniaNotFoundException {
        EstudanteResponse estudanteAtualizado = fachada.atualizarEstudante(estudanteUpdateRequest);
        return ResponseEntity.ok(estudanteAtualizado);
    }

    @DeleteMapping("/{id}/")
    public ResponseEntity<Void> deletarEstudante(@PathVariable Long id) throws EstudanteNotFoundException{
        fachada.deletarEstudante(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('GESTOR') and hasRole('PRAE_ACCESS')")
    @GetMapping("/credores")
    public ResponseEntity<Page<CredorResponse>> listarCredoresComBeneficiosAtivos(@PageableDefault(sort = "id") Pageable pageable) {
        Page<CredorResponse> credores = fachada.listarCredoresComBeneficiosAtivos(pageable);
        return ResponseEntity.ok(credores);
    }

    @PreAuthorize("hasRole('GESTOR') and hasRole('PRAE_ACCESS')")
    @GetMapping("/credores/beneficio/{id}")
    public ResponseEntity<Page<CredorResponse>> listarCredoresPorBeneficio(@PathVariable Long id, @PageableDefault(sort = "id") Pageable pageable) {
        Page<CredorResponse> credores = fachada.listarCredoresPorBeneficio(id, pageable);
        return ResponseEntity.ok(credores);
    }

    @PreAuthorize("hasRole('GESTOR') and hasRole('PRAE_ACCESS')")
    @GetMapping("/credores/publicacao")
    public ResponseEntity<Page<PublicacaoResponse>> listarCredoresParaPublicacao( @PageableDefault(sort = "id") Pageable pageable) {
        Page<AlunoResponse> pageAlunos = fachada.listarCredoresParaPublicacao(pageable);
        Page<PublicacaoResponse> pagePublicacoes = pageAlunos.map(alunoResponse ->
                new PublicacaoResponse(alunoResponse, modelMapper)
        );
        return ResponseEntity.ok(pagePublicacoes);
    }
    @PreAuthorize("hasRole('GESTOR') and hasRole('PRAE_ACCESS')")
    @GetMapping("/credores/curso/{id}")
    Page<CredorResponse> listarCredoresPorCurso(@PathVariable Long id, @PageableDefault(sort = "id") Pageable pageable) {
        return fachada.listarCredoresPorCurso(id, pageable);
    }



    @PreAuthorize("hasRole('GESTOR') and hasRole('PRAE_ACCESS')")
    @GetMapping("/{id}/relatorio")
    public ResponseEntity<RelatorioEstudanteAssistidoResponse> gerarRelatorioAssistido(
            @PathVariable Long id
    ) throws EstudanteNotFoundException {
        RelatorioEstudanteAssistidoResponse relatorio = fachada.gerarRelatorioEstudanteAssistido(id);
        return ResponseEntity.ok(relatorio);
    }

    @PreAuthorize("hasRole('ESTUDANTE')")
    @GetMapping("/current")
    public ResponseEntity<EstudanteResponse> buscarEstudanteAtual() throws EstudanteNotFoundException {
        EstudanteResponse estudante = fachada.buscarEstudanteAtual();
        return ResponseEntity.ok(estudante);
    }

    @PreAuthorize("hasAnyRole('GESTOR', 'PROFISSIONAL') and hasRole('PRAE_ACCESS')")
    @GetMapping("/{userId}")
    public ResponseEntity<EstudanteResponse> buscarEstudantePorUserId(@PathVariable UUID userId) throws EstudanteNotFoundException {
        EstudanteResponse estudante = fachada.buscarEstudantePorUserId(userId);
        return ResponseEntity.ok(estudante);
    }
}