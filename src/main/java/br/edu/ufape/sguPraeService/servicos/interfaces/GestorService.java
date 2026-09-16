package br.edu.ufape.sguPraeService.servicos.interfaces;

import br.edu.ufape.sguPraeService.exceptions.notFoundExceptions.GestorNotFoundException;
import br.edu.ufape.sguPraeService.exceptions.notFoundExceptions.UsuarioNotFoundException;
import br.edu.ufape.sguPraeService.models.Gestor;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.UUID;

public interface GestorService {
    Page<Gestor> listarGestores(Predicate predicate, Pageable pageable);
    Gestor buscarGestor(UUID id, boolean isAdm, UUID sessionId) throws GestorNotFoundException, UsuarioNotFoundException;
}