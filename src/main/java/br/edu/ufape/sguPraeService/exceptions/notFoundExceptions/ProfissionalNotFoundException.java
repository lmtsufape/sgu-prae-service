package br.edu.ufape.sguPraeService.exceptions.notFoundExceptions;

public class ProfissionalNotFoundException extends NotFoundException {
    public ProfissionalNotFoundException() {
        super("Profissional não encontrado");
    }
}