package br.edu.ufape.sguPraeService.comunicacao.annotations;

import br.edu.ufape.sguPraeService.comunicacao.validacoes.CepValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CepValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CepValido {
    String message() default "CEP inválido. O formato deve ser 00000-000.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
