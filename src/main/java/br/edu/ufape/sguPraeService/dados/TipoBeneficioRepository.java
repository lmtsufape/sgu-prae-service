package br.edu.ufape.sguPraeService.dados;

import br.edu.ufape.sguPraeService.models.TipoBeneficio;
import br.edu.ufape.sguPraeService.models.QTipoBeneficio;
import com.querydsl.core.types.dsl.StringPath;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.lang.NonNull;

public interface TipoBeneficioRepository extends JpaRepository<TipoBeneficio, Long>,
        QuerydslPredicateExecutor<TipoBeneficio>,
        QuerydslBinderCustomizer<QTipoBeneficio> {

    @Override
    default void customize(QuerydslBindings bindings, @NonNull QTipoBeneficio root) {
        bindings.bind(String.class).first((StringPath path, String value) -> path.containsIgnoreCase(value));
        bindings.excluding(root.ativo);
    }
}