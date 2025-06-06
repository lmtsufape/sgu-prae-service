package br.edu.ufape.sguPraeService.comunicacao.annotations;

import br.edu.ufape.sguPraeService.comunicacao.validacoes.HorarioContractValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = HorarioContractValidator.class)
@Documented
public @interface ConsistenteHorario {
    String message() default "Hora de início não pode ser posterior à hora de fim";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
