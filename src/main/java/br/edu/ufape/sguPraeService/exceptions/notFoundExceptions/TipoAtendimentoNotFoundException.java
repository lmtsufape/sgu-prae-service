package br.edu.ufape.sguPraeService.exceptions.notFoundExceptions;

public class TipoAtendimentoNotFoundException extends NotFoundException {
    public TipoAtendimentoNotFoundException() {
        super("Tipo de Atendimento não encontrado");
    }
}
