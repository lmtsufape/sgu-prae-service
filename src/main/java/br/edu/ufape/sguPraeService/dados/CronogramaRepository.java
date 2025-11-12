package br.edu.ufape.sguPraeService.dados;

import br.edu.ufape.sguPraeService.models.Cronograma;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.*;

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
            if (value.isEmpty()) {
                return Optional.empty();
            }
            List<LocalDate> datas = new ArrayList<>(value);
            Collections.sort(datas);
            if (datas.size() == 1) {
                // 1. Filtro de data exata
                return Optional.of(path.eq(datas.getFirst()));
            } else {
                // 2. Filtro de período
                return Optional.of(path.between(datas.getFirst(), datas.getLast()));
            }
        });

        bindings.bind(root.profissional.id).first((path, value) -> path.eq(value));
        bindings.bind(root.tipoAtendimento.id).first((path, value) -> path.eq(value));
        bindings.excluding(root.ativo, root.vagas);
    }
}