package br.edu.ufape.sguPraeService.exceptions.notFoundExceptions;

public class VagaNotFoundException extends NotFoundException {
    public VagaNotFoundException() {
        super("Vaga não encontrada");
    }
}