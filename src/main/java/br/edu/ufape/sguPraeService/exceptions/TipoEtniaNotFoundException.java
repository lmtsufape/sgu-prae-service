package br.edu.ufape.sguPraeService.exceptions;

import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Tipo de etnia não encontrado")
public class TipoEtniaNotFoundException extends ChangeSetPersister.NotFoundException {
}
