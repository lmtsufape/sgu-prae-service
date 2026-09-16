package br.edu.ufape.sguPraeService.exceptions.notFoundExceptions;

public class UsuarioNotFoundException extends NotFoundException {
    public UsuarioNotFoundException() {
        super("Usuario não encontrada");
    }
}
