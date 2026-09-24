package br.edu.ufape.sguPraeService.comunicacao.controllers;

import br.edu.ufape.sguPraeService.comunicacao.dto.gestor.GestorResponse;
import br.edu.ufape.sguPraeService.exceptions.notFoundExceptions.GestorNotFoundException;
import br.edu.ufape.sguPraeService.exceptions.notFoundExceptions.UsuarioNotFoundException;
import br.edu.ufape.sguPraeService.fachada.Fachada;
import br.edu.ufape.sguPraeService.models.Gestor;
import br.edu.ufape.sguPraeService.models.Usuario;
import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/gestor")
public class GestorController {

    private final Fachada fachada;
    private final ModelMapper modelMapper;

    @GetMapping("/{id}")
    public ResponseEntity<GestorResponse> buscarGestor(@PathVariable UUID id) throws GestorNotFoundException, UsuarioNotFoundException {
        // A fachada agora retorna Gestor, então o GestorResponse vai aceitar sem reclamar!
        Gestor response = fachada.buscarGestor(id);
        return new ResponseEntity<>(new GestorResponse(response, modelMapper), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @GetMapping
    public Page<GestorResponse> listarGestores(
            @QuerydslPredicate(root = Gestor.class) Predicate predicate, // Mudei de Usuario.class para Gestor.class
            @PageableDefault(value = 10)
            @SortDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {

        return fachada.listarGestores(predicate, pageable)
                .map(gestor -> new GestorResponse(gestor, modelMapper)); // Nomes limpos
    }
}