package br.edu.ufape.sguPraeService.dados;

import br.edu.ufape.sguPraeService.models.Cronograma;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

import br.edu.ufape.sguPraeService.models.QCronograma;
import com.querydsl.core.types.dsl.StringPath;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.lang.NonNull;

@Repository
public interface CronogramaRepository extends JpaRepository<Cronograma, Long>,
        QuerydslPredicateExecutor<Cronograma>,
        QuerydslBinderCustomizer<QCronograma> {


    Page<Cronograma> findAllByAtivoTrue(Pageable pageable);
    Page<Cronograma> findByAtivoTrueAndTipoAtendimento_Id(Long id, Pageable pageable);

    Page<Cronograma> findAllByAtivoTrueAndProfissional_UserId(UUID profissionalUserId, Pageable pageable);

    boolean existsByTipoAtendimento_Id(Long tipoAtendimentoId);

    @Override
    default void customize(QuerydslBindings bindings, @NonNull QCronograma root) {
        bindings.bind(String.class).first((StringPath path, String value) -> path.containsIgnoreCase(value));
        bindings.bind(root.data).all((path, value) -> {
            // Exemplo simples: equals
            return Optional.of(path.eq(value.iterator().next()));
        });
        bindings.bind(root.profissional.id).first((path, value) -> path.eq(value));
        bindings.bind(root.tipoAtendimento.id).first((path, value) -> path.eq(value));
        bindings.excluding(root.ativo, root.vagas);
    }
}