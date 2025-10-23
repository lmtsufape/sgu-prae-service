package br.edu.ufape.sguPraeService.dados;

import br.edu.ufape.sguPraeService.models.TipoAtendimento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import br.edu.ufape.sguPraeService.models.QTipoAtendimento;
import com.querydsl.core.types.dsl.StringPath;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.lang.NonNull;

@Repository
public interface TipoAtendimentoRepository extends JpaRepository<TipoAtendimento, Long>,
        QuerydslPredicateExecutor<TipoAtendimento>,
        QuerydslBinderCustomizer<QTipoAtendimento> {

    @Override
    default void customize(QuerydslBindings bindings, @NonNull QTipoAtendimento root) {
        bindings.bind(String.class).first((StringPath path, String value) -> path.containsIgnoreCase(value));
        bindings.excluding(root.horarios);
    }
}
