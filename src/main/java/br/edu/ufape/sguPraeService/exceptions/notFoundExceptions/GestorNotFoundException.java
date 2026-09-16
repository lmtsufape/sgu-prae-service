package br.edu.ufape.sguPraeService.exceptions.notFoundExceptions;

public class GestorNotFoundException extends NotFoundException {
    public GestorNotFoundException() {
        super("Gestor não encontrado");
    }
}
