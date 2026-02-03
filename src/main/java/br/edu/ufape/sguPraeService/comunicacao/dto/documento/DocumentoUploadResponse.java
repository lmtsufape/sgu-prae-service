package br.edu.ufape.sguPraeService.comunicacao.dto.documento;

import br.edu.ufape.sguPraeService.models.Documento;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DocumentoUploadResponse {
    private Long id;
    private String nome;
    private String path;

    public DocumentoUploadResponse(Documento documento) {
        this.id = documento.getId();
        this.nome = documento.getNome();
        this.path = documento.getPath();
    }
}
