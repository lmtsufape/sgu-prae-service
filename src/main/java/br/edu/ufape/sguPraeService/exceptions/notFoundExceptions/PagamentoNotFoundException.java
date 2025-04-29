package br.edu.ufape.sguPraeService.exceptions.notFoundExceptions;

public class PagamentoNotFoundException extends NotFoundException {
    public PagamentoNotFoundException() {
        super("Pagamento não encontrado");
    }
}