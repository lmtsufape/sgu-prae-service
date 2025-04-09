package br.edu.ufape.sguPraeService.exceptions;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;

public class ExceptionUtil {

    public static RuntimeException handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        Throwable cause = e.getCause();
        if (cause instanceof ConstraintViolationException constraintViolationException) {
            String message= constraintViolationException.getMessage();
            if(message.contains("tipo_etnia_tipo_key")){throw new UniqueConstraintViolationException("tipo", "Etnia já cadastrada!");}
            else if (message.contains("estudante_user_id_key")){throw new UniqueConstraintViolationException("estudante", "Estudante já cadastrado!");}
            else if (message.contains("profissional_user_id_key")){throw new UniqueConstraintViolationException("profissional", "Profissional já cadastrado!");}
        }
        return new IllegalArgumentException("Erro de integridade de dados!");
    }

}