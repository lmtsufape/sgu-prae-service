package br.edu.ufape.sguPraeService.exceptions.notFoundExceptions;


public class TipoEtniaNotFoundException extends NotFoundException {
    public TipoEtniaNotFoundException() {
        super("Tipo de etnia não encontrado");
    }
}