package br.edu.ufape.sguPraeService.exceptions;

import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = org.springframework.http.HttpStatus.NOT_FOUND, reason = "Tipo Auxílio não encontrado")
public class TipoAuxilioNotFoundException extends ChangeSetPersister.NotFoundException {
}
