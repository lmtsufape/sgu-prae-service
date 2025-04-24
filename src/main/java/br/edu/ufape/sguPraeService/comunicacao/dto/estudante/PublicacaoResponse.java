package br.edu.ufape.sguPraeService.comunicacao.dto.estudante;

import br.edu.ufape.sguPraeService.comunicacao.dto.usuario.AlunoResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import java.util.UUID;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class PublicacaoResponse {
    UUID id;
    String nome;

    public PublicacaoResponse(AlunoResponse alunoResponse, ModelMapper modelMapper) {
        if (alunoResponse == null) throw new IllegalArgumentException("O aluno não pode ser nulo");
        else modelMapper.map(alunoResponse, this);
    }
}
