package br.edu.ufape.sguPraeService.exceptions.notFoundExceptions;

public class EnderecoNotFoundException extends NotFoundException {
    public EnderecoNotFoundException() {
        super("Endereço não encontrado");
    }
}