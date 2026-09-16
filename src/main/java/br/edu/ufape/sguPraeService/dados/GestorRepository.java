package br.edu.ufape.sguPraeService.dados;

import br.edu.ufape.sguPraeService.models.Gestor;
import br.edu.ufape.sguPraeService.models.QGestor;
import com.querydsl.core.types.dsl.StringPath;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.lang.NonNull;

import java.util.UUID;

public interface GestorRepository extends JpaRepository<Gestor, UUID>, QuerydslPredicateExecutor<Gestor>, QuerydslBinderCustomizer<QGestor> {
    @Override
    default void customize(QuerydslBindings bindings, @NonNull QGestor root) {
        bindings.bind(String.class).first((StringPath path, String value) -> path.containsIgnoreCase(value));
        bindings.excluding(root.ativo);
    }
}