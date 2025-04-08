package br.edu.ufape.sguPraeService.comunicacao.validacoes;

import br.edu.ufape.sguPraeService.comunicacao.annotations.TipoDeficienciaObrigatorio;
import br.edu.ufape.sguPraeService.comunicacao.dto.estudante.EstudanteRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class TipoDeficienciaObrigatorioValidator implements ConstraintValidator<TipoDeficienciaObrigatorio, EstudanteRequest> {

    @Override
    public boolean isValid(EstudanteRequest request, ConstraintValidatorContext context) {
        if (request == null) return true; // ou false, dependendo da lógica do seu sistema

        // Se deficiente == true, tipoDeficiencia deve estar preenchido
        if (request.isDeficiente() && (request.getTipoDeficiencia() == null || request.getTipoDeficiencia().isBlank())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                    .addPropertyNode("tipoDeficiencia")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}