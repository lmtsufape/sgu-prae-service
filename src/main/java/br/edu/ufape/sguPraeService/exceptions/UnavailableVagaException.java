package br.edu.ufape.sguPraeService.exceptions;

public class UnavailableVagaException extends RuntimeException {
    public UnavailableVagaException() {
      super("A vaga já está reservada.");
    }
}
