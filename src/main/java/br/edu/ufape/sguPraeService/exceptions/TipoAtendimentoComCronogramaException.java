package br.edu.ufape.sguPraeService.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Tipo de atendimento possui cronogramas relacionados")
public class TipoAtendimentoComCronogramaException extends RuntimeException {
    public TipoAtendimentoComCronogramaException() {
        super("Não é possível excluir este tipo de atendimento, pois existem atendimentos associados a ele.");
    }
}