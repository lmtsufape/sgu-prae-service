package br.edu.ufape.sguPraeService.exceptions.notFoundExceptions;

public class DocumentoNotFoundException extends NotFoundException {
    public DocumentoNotFoundException(Long id) {
        super("Documento com id " + id + " não encontrado");
    }

    public DocumentoNotFoundException(String nome) {
        super("Documento com nome " + nome + " não encontrado");
    }
}

