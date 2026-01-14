package br.edu.ufape.sguPraeService.exceptions.notFoundExceptions;

public class EstudanteNotFoundException extends NotFoundException {
    public EstudanteNotFoundException() {
        super("Estudante não encontrado");
    }

    public EstudanteNotFoundException(String message) {
        super(message);
    }
}