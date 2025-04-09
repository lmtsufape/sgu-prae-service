package br.edu.ufape.sguPraeService.exceptions.notFoundExceptions;

public class CronogramaNotFoundException extends NotFoundException {
    public CronogramaNotFoundException() {
        super("Cronograma não encontrado");
    }
}