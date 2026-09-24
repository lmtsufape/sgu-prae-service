package br.edu.ufape.sguPraeService.servicos;

import br.edu.ufape.sguPraeService.dados.GestorRepository;
import br.edu.ufape.sguPraeService.exceptions.GlobalAccessDeniedException;
import br.edu.ufape.sguPraeService.exceptions.notFoundExceptions.GestorNotFoundException;
import br.edu.ufape.sguPraeService.exceptions.notFoundExceptions.UsuarioNotFoundException;
import br.edu.ufape.sguPraeService.models.Gestor;
import br.edu.ufape.sguPraeService.models.QGestor;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GestorService implements br.edu.ufape.sguPraeService.servicos.interfaces.GestorService {

    private final GestorRepository gestorRepository;

    @Override
    public Page<Gestor> listarGestores(Predicate predicate, Pageable pageable) {
        QGestor qGestor = QGestor.gestor;
        BooleanBuilder filtroFixo = new BooleanBuilder();
        filtroFixo.and(qGestor.ativo.isTrue());

        Predicate predicadoFinal = filtroFixo.and(predicate);

        return gestorRepository.findAll(predicadoFinal, pageable);
    }

    @Override
    public Gestor buscarGestor(UUID id, boolean isAdm, UUID sessionId) throws GestorNotFoundException, UsuarioNotFoundException {
        // Como estamos no GestorRepository, se o UUID for de um Aluno, ele vai dar NotFound automaticamente!
        Gestor gestor = gestorRepository.findById(id).orElseThrow(GestorNotFoundException::new);

        if(!isAdm && !gestor.getId().equals(sessionId)) {
            throw new GlobalAccessDeniedException("Você não tem permissão para acessar este recurso");
        }

        return gestor;
    }
}