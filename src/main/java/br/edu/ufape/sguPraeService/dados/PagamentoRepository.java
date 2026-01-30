package br.edu.ufape.sguPraeService.dados;

import br.edu.ufape.sguPraeService.models.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import br.edu.ufape.sguPraeService.models.QPagamento;
import com.querydsl.core.types.dsl.StringPath;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.lang.NonNull;
import org.springframework.data.jpa.repository.Query;

public interface PagamentoRepository extends JpaRepository<Pagamento, Long>,
        QuerydslPredicateExecutor<Pagamento>,
        QuerydslBinderCustomizer<QPagamento> {

    List<Pagamento> findByValorBetween(BigDecimal min, BigDecimal max);
    Page<Pagamento> findByValorBetween(BigDecimal min, BigDecimal max, Pageable pageable);

    List<Pagamento> findByAnoReferenciaAndMesReferencia(Integer anoReferencia, Integer mesReferencia);
    List<Pagamento> findByAnoReferenciaAndMesReferenciaAndNumeroLote(Integer anoReferencia, Integer mesReferencia, String numeroLote);

    @Override
    default void customize(QuerydslBindings bindings, @NonNull QPagamento root) {
        bindings.bind(String.class).first((StringPath path, String value) -> path.containsIgnoreCase(value));

        bindings.bind(root.numeroLote).first((path, value) -> path.eq(value));

        bindings.bind(root.mesReferencia).first((path, value) -> path.eq(value));
        bindings.bind(root.anoReferencia).first((path, value) -> path.eq(value));

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

        bindings.bind(root.valor).all((path, value) -> {
            if (value.isEmpty()) {
                return Optional.empty();
            }
            List<BigDecimal> valores = new ArrayList<>(value);
            Collections.sort(valores);

            if (valores.size() == 1) {
                // 1. Filtro de valor exato
                return Optional.of(path.eq(valores.getFirst()));
            } else {
                // 2. Filtro between
                return Optional.of(path.between(valores.getFirst(), valores.getLast()));
            }
        });

        bindings.bind(root.beneficio.id).first((path, value) -> path.eq(value));
        bindings.excluding(root.ativo);
    }

    @Query("SELECT COALESCE(SUM(p.valor), 0) FROM Pagamento p WHERE p.ativo = true")
    BigDecimal findTotalPagamentosAtivos();

    @Query("SELECT b.tipoBeneficio.id, b.tipoBeneficio.descricao, SUM(p.valor) " +
           "FROM Pagamento p JOIN p.beneficio b " +
           "WHERE p.ativo = true " +
           "GROUP BY b.tipoBeneficio.id, b.tipoBeneficio.descricao")
    List<Object[]> findValorTotalPorTipoBeneficio();
}