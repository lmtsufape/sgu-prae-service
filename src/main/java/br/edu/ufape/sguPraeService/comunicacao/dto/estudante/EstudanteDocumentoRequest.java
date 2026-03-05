package br.edu.ufape.sguPraeService.comunicacao.dto.estudante;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class EstudanteDocumentoRequest {

    @NotEmpty(message = "É necessário enviar os documentos (CPF, RG/CNH e Comprovante).")
    private List<MultipartFile> arquivos;

}