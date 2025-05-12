package br.edu.ufape.sguPraeService.comunicacao.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import br.edu.ufape.sguPraeService.comunicacao.validacoes.AuxilioValidoValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Constraint(validatedBy = AuxilioValidoValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface AuxilioValido {
    String message() default "Auxílio Inválido!";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
