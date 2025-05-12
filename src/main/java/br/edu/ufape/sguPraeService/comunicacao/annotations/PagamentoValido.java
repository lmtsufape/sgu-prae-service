package br.edu.ufape.sguPraeService.comunicacao.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import br.edu.ufape.sguPraeService.comunicacao.validacoes.PagamentoValidoValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Constraint(validatedBy = PagamentoValidoValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface PagamentoValido {
    String message() default "Pagamento inválido!";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
