package br.edu.ufape.sguPraeService.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Etnia já cadastrada!")
public class TipoEtniaDuplicadoException extends RuntimeException {
    public TipoEtniaDuplicadoException(String message) {
        super(message);
    }
}