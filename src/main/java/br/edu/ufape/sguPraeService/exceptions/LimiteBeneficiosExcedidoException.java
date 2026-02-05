package br.edu.ufape.sguPraeService.exceptions;

public class LimiteBeneficiosExcedidoException extends RuntimeException {
    public LimiteBeneficiosExcedidoException(String message) {
        super(message);
    }
}