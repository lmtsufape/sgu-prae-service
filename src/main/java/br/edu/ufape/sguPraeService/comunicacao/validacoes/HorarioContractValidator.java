package br.edu.ufape.sguPraeService.comunicacao.validacoes;

import br.edu.ufape.sguPraeService.comunicacao.annotations.ConsistenteHorario;
import br.edu.ufape.sguPraeService.comunicacao.dto.vaga.VagaRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalTime;

public class HorarioContractValidator implements ConstraintValidator<ConsistenteHorario, VagaRequest> {

    @Override
    public void initialize(ConsistenteHorario constraintAnnotation) {
        // Pode deixar vazio
    }

    @Override
    public boolean isValid(VagaRequest request, ConstraintValidatorContext context) {
        if (request == null) {
            return true; // deixe nulo passar; se quiser proibir, combine com @NotNull na classe
        }

        LocalTime inicio = request.getHoraInicio();
        LocalTime fim = request.getHoraFim();

        // Se algum horário for nulo, considere válido (ou trate à parte com @NotNull)
        if (inicio == null || fim == null) {
            return true;
        }

        return !inicio.isAfter(fim);
    }
}