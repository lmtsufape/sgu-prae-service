package br.edu.ufape.sguPraeService.comunicacao.controllers;

import br.edu.ufape.sguPraeService.comunicacao.dto.documento.DocumentoUploadResponse;
import br.edu.ufape.sguPraeService.exceptions.notFoundExceptions.DocumentoNotFoundException;
import br.edu.ufape.sguPraeService.fachada.Fachada;
import br.edu.ufape.sguPraeService.models.Documento;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/upload/documento")
public class DocumentoController {

    private final Fachada fachada;


    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<DocumentoUploadResponse> uploadDocumento(
            @Parameter(description = "Arquivo a ser enviado", required = true)
            @RequestPart("arquivo") MultipartFile arquivo) {
        Documento documento = fachada.uploadDocumento(arquivo);
        return ResponseEntity.status(HttpStatus.CREATED).body(new DocumentoUploadResponse(documento));
    }


    @GetMapping
    public ResponseEntity<List<DocumentoUploadResponse>> listarTodosDocumentos() {
        List<Documento> documentos = fachada.listarTodosDocumentos();
        List<DocumentoUploadResponse> responses = documentos.stream()
                .map(DocumentoUploadResponse::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/usuario")
    public ResponseEntity<List<DocumentoUploadResponse>> listarDocumentosDoUsuario() {
        List<Documento> documentos = fachada.listarDocumentosDoUsuario();
        List<DocumentoUploadResponse> responses = documentos.stream()
                .map(DocumentoUploadResponse::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }


    @GetMapping("/{id}")
    public ResponseEntity<DocumentoUploadResponse> buscarDocumento(@PathVariable Long id)
            throws DocumentoNotFoundException {
        Documento documento = fachada.buscarDocumento(id);
        return ResponseEntity.ok(new DocumentoUploadResponse(documento));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarDocumento(@PathVariable Long id)
            throws DocumentoNotFoundException {
        fachada.deletarDocumento(id);
        return ResponseEntity.noContent().build();
    }
}
