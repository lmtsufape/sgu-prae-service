package br.edu.ufape.sguPraeService.exceptions.notFoundExceptions;

public class AgendamentoNotFoundException extends NotFoundException {
  public AgendamentoNotFoundException() {
    super("Agendamento não encontrado");
  }
}
