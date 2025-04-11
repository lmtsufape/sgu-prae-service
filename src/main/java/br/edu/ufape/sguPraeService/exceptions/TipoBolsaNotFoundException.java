package br.edu.ufape.sguPraeService.exceptions;

import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = org.springframework.http.HttpStatus.NOT_FOUND, reason = "Tipo Bolsa não encontrada")
public class TipoBolsaNotFoundException extends ChangeSetPersister.NotFoundException {
}
