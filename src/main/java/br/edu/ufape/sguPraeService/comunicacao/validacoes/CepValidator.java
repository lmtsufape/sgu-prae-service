package br.edu.ufape.sguPraeService.comunicacao.validacoes;

import br.edu.ufape.sguPraeService.comunicacao.annotations.CepValido;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CepValidator implements ConstraintValidator<CepValido, String> {

    private static final String CEP_REGEX = "^\\d{5}-\\d{3}$";

    @Override
    public boolean isValid(String cep, ConstraintValidatorContext context) {
        if (cep == null || cep.isBlank()) {
            return true;
        }

        return cep.matches(CEP_REGEX);
    }
}