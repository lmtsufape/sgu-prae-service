package br.edu.ufape.sguPraeService.exceptions.notFoundExceptions;

public abstract class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
