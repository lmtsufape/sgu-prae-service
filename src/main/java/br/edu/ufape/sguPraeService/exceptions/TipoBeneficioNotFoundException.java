package br.edu.ufape.sguPraeService.exceptions;

import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = org.springframework.http.HttpStatus.NOT_FOUND, reason = "Tipo Beneficio não encontrado")
public class TipoBeneficioNotFoundException extends ChangeSetPersister.NotFoundException {
}
