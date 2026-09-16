package br.edu.ufape.sguPraeService.comunicacao.controllers;

import br.edu.ufape.sguPraeService.comunicacao.dto.estudante.EstudanteRequest;
import br.edu.ufape.sguPraeService.comunicacao.dto.profissional.ProfissionalRequest;
import br.edu.ufape.sguPraeService.comunicacao.dto.gestor.GestorRequest;
import br.edu.ufape.sguPraeService.comunicacao.dto.usuario.UsuarioPatchRequest;
import br.edu.ufape.sguPraeService.comunicacao.dto.usuario.UsuarioResponse;
import br.edu.ufape.sguPraeService.exceptions.notFoundExceptions.UsuarioNotFoundException;
import br.edu.ufape.sguPraeService.fachada.Fachada;
import br.edu.ufape.sguPraeService.models.Usuario;
import com.querydsl.core.types.Predicate;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/usuario")
public class UsuarioController {

    private final Fachada fachada;
    private final ModelMapper modelMapper;

    // ================= ROTAS DE CRIAÇÃO (ESPECÍFICAS) ================= //

    @PostMapping(value = "/estudante", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UsuarioResponse> cadastrarEstudante(
            @RequestPart("dados") @Valid EstudanteRequest request,
            @RequestPart(value = "arquivos", required = false) List<MultipartFile> arquivos) throws Exception {
        Usuario response = fachada.cadastrarEstudante(request, arquivos);
        return new ResponseEntity<>(new UsuarioResponse(response, modelMapper), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('GESTOR', 'ADMINISTRADOR')")
    @PostMapping("/profissional")
    public ResponseEntity<UsuarioResponse> cadastrarProfissional(@Valid @RequestBody ProfissionalRequest request) throws Exception {
        Usuario response = fachada.cadastrarProfissional(request);
        return new ResponseEntity<>(new UsuarioResponse(response, modelMapper), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PostMapping("/gestor")
    public ResponseEntity<UsuarioResponse> cadastrarGestor(@Valid @RequestBody GestorRequest request) throws Exception {
        Usuario response = fachada.cadastrarGestor(request);
        return new ResponseEntity<>(new UsuarioResponse(response, modelMapper), HttpStatus.CREATED);
    }

    // ================= ROTAS GERAIS DE USUÁRIO ================= //

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> buscar(@PathVariable UUID id) throws UsuarioNotFoundException {
        Usuario response = fachada.buscarUsuario(id);
        return new ResponseEntity<>(new UsuarioResponse(response, modelMapper), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @GetMapping
    public Page<UsuarioResponse> listarUsuarios(
            @QuerydslPredicate(root = Usuario.class) Predicate predicate,
            @PageableDefault(value = 10)
            @SortDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        return fachada.listarUsuarios(predicate, pageable).map(usuario -> new UsuarioResponse(usuario, modelMapper));
    }

    @GetMapping("/current")
    public ResponseEntity<UsuarioResponse> buscarUsuarioAtual() throws UsuarioNotFoundException {
        Usuario response = fachada.buscarUsuarioAtual();
        return new ResponseEntity<>(new UsuarioResponse(response, modelMapper), HttpStatus.OK);
    }

    @PatchMapping
    public ResponseEntity<UsuarioResponse> atualizar(@Valid @RequestBody UsuarioPatchRequest usuario) throws UsuarioNotFoundException {
        Usuario atualizado = fachada.editarUsuario(usuario);
        return ResponseEntity.ok(new UsuarioResponse(atualizado, modelMapper));
    }

    @DeleteMapping
    public ResponseEntity<Void> deletar() throws UsuarioNotFoundException {
        fachada.deletarUsuario();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarUsuario(@PathVariable UUID id) throws UsuarioNotFoundException {
        fachada.deletarUsuario(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}