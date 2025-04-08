package br.edu.ufape.sguPraeService.comunicacao.validacoes;

import br.edu.ufape.sguPraeService.comunicacao.annotations.NumeroValido;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NumeroValidoValidator implements ConstraintValidator<NumeroValido, String> {

    // Expressão regular para validar o formato (XX)XXXXX-XXXX
    private static final String PHONE_NUMBER_PATTERN = "^\\(\\d{2}\\) \\d{5}-\\d{4}$";


    @Override
    public boolean isValid(String phoneNumber, ConstraintValidatorContext context) {
        if (phoneNumber == null || phoneNumber.isBlank()) {
            return false;
        }
        return phoneNumber.matches(PHONE_NUMBER_PATTERN);
    }

}
