package br.edu.ufape.sguPraeService.servicos.interfaces;


import br.edu.ufape.sguPraeService.exceptions.EnderecoNotFoundException;
import br.edu.ufape.sguPraeService.models.Endereco;

import java.util.List;
import java.util.Optional;

public interface EnderecoService {

    List<Endereco> listarEnderecos();

    Endereco buscarEndereco(Long id) throws EnderecoNotFoundException;

    Endereco criarEndereco(Endereco endereco);

    void excluirEndereco(Long id);

    Endereco editarEndereco(Long id, Endereco enderecoAtualizado);
}
