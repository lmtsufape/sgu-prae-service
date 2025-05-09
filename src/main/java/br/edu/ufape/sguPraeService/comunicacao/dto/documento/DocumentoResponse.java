package br.edu.ufape.sguPraeService.comunicacao.dto.documento;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DocumentoResponse {
    private String nome;
    private String base64;
}
