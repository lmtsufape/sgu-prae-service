package br.edu.ufape.sguPraeService.dados;

import br.edu.ufape.sguPraeService.models.Profissional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import br.edu.ufape.sguPraeService.models.QProfissional;
import com.querydsl.core.types.dsl.StringPath;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.lang.NonNull;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProfissionalRepository extends JpaRepository<Profissional, Long>,
        QuerydslPredicateExecutor<Profissional>,
        QuerydslBinderCustomizer<QProfissional> {

    Optional<Profissional> findByUserId(UUID userId);

    @Override
    default void customize(QuerydslBindings bindings, @NonNull QProfissional root) {
        bindings.bind(String.class).first((StringPath path, String value) -> path.containsIgnoreCase(value));
        bindings.excluding(root.ativo);
    }
}
