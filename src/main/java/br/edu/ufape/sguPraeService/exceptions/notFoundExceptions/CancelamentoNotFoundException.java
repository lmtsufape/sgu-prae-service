package br.edu.ufape.sguPraeService.exceptions.notFoundExceptions;

public class CancelamentoNotFoundException extends NotFoundException {
    public CancelamentoNotFoundException() {

      super("Cancelamento não encontrado");
    }
}
