package br.edu.ufape.sguPraeService.comunicacao.dto.documento;

import org.modelmapper.ModelMapper;

import br.edu.ufape.sguPraeService.models.Documento;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class DocumentoResponse {

    Long id;
    private String nome;
    private String path;

    public DocumentoResponse(Documento documento, ModelMapper modelMapper){
        if (documento == null) throw new IllegalArgumentException("Documento não pode ser nulo");
        else modelMapper.map(documento, this);
    }
}
