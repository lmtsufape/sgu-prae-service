package br.edu.ufape.sguPraeService.comunicacao.annotations;

import br.edu.ufape.sguPraeService.comunicacao.validacoes.TipoDeficienciaObrigatorioValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = TipoDeficienciaObrigatorioValidator.class)
@Documented
public @interface TipoDeficienciaObrigatorio {
    String message() default "O tipo de deficiência é obrigatório quando o estudante é deficiente.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}