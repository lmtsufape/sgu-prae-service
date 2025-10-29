package br.edu.ufape.sguPraeService.dados;

import br.edu.ufape.sguPraeService.models.Beneficio;
import br.edu.ufape.sguPraeService.models.Estudante;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import br.edu.ufape.sguPraeService.models.QBeneficio;
import com.querydsl.core.types.dsl.StringPath;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.lang.NonNull;


public interface BeneficioRepository extends JpaRepository<Beneficio, Long>,
        QuerydslPredicateExecutor<Beneficio>,
        QuerydslBinderCustomizer<QBeneficio> {

  List<Beneficio> findByAtivoTrue();


  Page<Beneficio> findByTipoBeneficioId(@Param("tipoId") Long tipoId, Pageable pageable);

  List<Beneficio> findByPagamentos_Id(Long pagamentoId);

  List<Beneficio> findAllByAtivoTrueAndEstudantes_Id(Long estudanteId);

  Page<Beneficio> findAllByAtivoTrueAndEstudantes_Id(Long estudanteId, Pageable pageable);

  @Query("SELECT b.estudantes FROM Beneficio b WHERE b.ativo = true")
  Page<Estudante> findAllEstudantesByAtivoTrue(Pageable pageable);

  @Query("SELECT b.estudantes FROM Beneficio b WHERE b.ativo = true AND b.id = :beneficioId")
  List<Estudante> findEstudantesByBeneficioId(@Param("beneficioId") Long beneficioId);

  @Query("SELECT b.estudantes FROM Beneficio b WHERE b.ativo = true AND b.id = :beneficioId")
  Page<Estudante> findEstudantesByBeneficioId(@Param("beneficioId") Long beneficioId, Pageable pageable);


  @Override
  default void customize(QuerydslBindings bindings, @NonNull QBeneficio root) {
    bindings.bind(String.class).first((StringPath path, String value) -> path.containsIgnoreCase(value));
    bindings.excluding(root.ativo, root.termo, root.pagamentos);
    bindings.bind(root.estudantes.id).first((path, value) -> path.eq(value));
    bindings.bind(root.tipoBeneficio.id).first((path, value) -> path.eq(value));
  }
}