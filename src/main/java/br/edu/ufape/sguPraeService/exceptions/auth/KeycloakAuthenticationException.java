package br.edu.ufape.sguPraeService.exceptions.auth;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class KeycloakAuthenticationException extends RuntimeException {
  public KeycloakAuthenticationException(String message) {
    super(message);
  }

  public KeycloakAuthenticationException(String message, Throwable cause) {
    super(message, cause);
  }
}