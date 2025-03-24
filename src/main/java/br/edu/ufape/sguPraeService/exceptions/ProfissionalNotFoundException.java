package br.edu.ufape.sguPraeService.exceptions;

import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = org.springframework.http.HttpStatus.NOT_FOUND, reason = "Profissional não encontrado")
public class ProfissionalNotFoundException extends ChangeSetPersister.NotFoundException {
}
