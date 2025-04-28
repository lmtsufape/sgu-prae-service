package br.edu.ufape.sguPraeService.exceptions;

public class EstudanteSemAuxilioAtivoException extends RuntimeException {
  public EstudanteSemAuxilioAtivoException() {
    super("O estudante informado não possui auxílios ativos.");
  }
}
