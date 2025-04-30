package br.edu.ufape.sguPraeService.comunicacao.dto.documento;

import org.modelmapper.ModelMapper;

import br.edu.ufape.sguPraeService.models.Documento;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class DocumentoRequest {

    @NotBlank(message = "Nome é obrigatório")
    private String nome;
    @NotBlank(message = "Path é obrigatório")
    private String path;

    public Documento convertToEntity(DocumentoRequest documentoRequest, ModelMapper modelMapper) {
        return modelMapper.map(documentoRequest, Documento.class);
    }
}