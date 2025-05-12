package br.edu.ufape.sguPraeService.comunicacao.validacoes;

import br.edu.ufape.sguPraeService.comunicacao.annotations.AuxilioValido;
import br.edu.ufape.sguPraeService.comunicacao.dto.auxilio.AuxilioRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class AuxilioValidoValidator implements ConstraintValidator<AuxilioValido, AuxilioRequest> {

    @Override
    public boolean isValid(AuxilioRequest auxilioRequest, ConstraintValidatorContext context) {
        if (auxilioRequest == null) {
            return false;
        }

        if (auxilioRequest.getTipoAuxilioId() == null && auxilioRequest.getTipoBolsaId() == null) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Obrigatório TipoAuxilio ou TipoBolsa")
                    .addPropertyNode("tipoAuxilioId")
                    .addConstraintViolation();
            context.buildConstraintViolationWithTemplate("Obrigatório TipoAuxilio ou TipoBolsa")
                    .addPropertyNode("tipoBolsaId")
                    .addConstraintViolation();
            return false;
        }

        if (auxilioRequest.getTipoAuxilioId() != null && auxilioRequest.getTipoBolsaId() != null) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Escolha somente um TipoAuxilio ou um TipoBolsa")
                    .addPropertyNode("tipoAuxilioId")
                    .addConstraintViolation();
            context.buildConstraintViolationWithTemplate("Escolha somente um TipoAuxilio ou um TipoBolsa")
                    .addPropertyNode("tipoBolsaId")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}