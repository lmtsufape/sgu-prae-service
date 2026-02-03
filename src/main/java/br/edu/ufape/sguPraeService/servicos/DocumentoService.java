package br.edu.ufape.sguPraeService.servicos;

import br.edu.ufape.sguPraeService.dados.DocumentoRepository;
import br.edu.ufape.sguPraeService.exceptions.notFoundExceptions.DocumentoNotFoundException;
import br.edu.ufape.sguPraeService.models.Documento;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Log4j2
@Service
@RequiredArgsConstructor
public class DocumentoService implements br.edu.ufape.sguPraeService.servicos.interfaces.DocumentoService {

    private final DocumentoRepository documentoRepository;
    private final List<String> tiposPermitidos = List.of(
            "application/pdf",
            "image/jpeg",
            "image/png",
            "image/jpg"
    );

    @Value("${arquivo.diretorio-upload}")
    private String uploadDir;

    @Override
    @Transactional
    public Documento salvar(MultipartFile arquivo) {
        validarTipoArquivo(arquivo);

        String uuid = UUID.randomUUID().toString();
        String extensao = FilenameUtils.getExtension(arquivo.getOriginalFilename());
        String nomeArquivoComUUID = uuid + "." + extensao;

        try {
            Path diretorio = Paths.get(uploadDir);
            if (!Files.exists(diretorio)) {
                Files.createDirectories(diretorio);
            }

            Path caminho = diretorio.resolve(nomeArquivoComUUID);
            Files.copy(arquivo.getInputStream(), caminho);

            Documento documento = new Documento();
            documento.setNome(nomeArquivoComUUID);
            documento.setPath(caminho.toString());

            log.info("Documento salvo com sucesso: {}", nomeArquivoComUUID);
            return documentoRepository.save(documento);
        } catch (IOException e) {
            log.error("Erro ao salvar arquivo: {}", e.getMessage());
            throw new RuntimeException("Falha ao salvar arquivo: " + arquivo.getOriginalFilename(), e);
        }
    }

    @Override
    public Documento buscar(Long id) throws DocumentoNotFoundException {
        return documentoRepository.findById(id)
                .orElseThrow(() -> new DocumentoNotFoundException(id));
    }

    @Override
    @Transactional
    public void deletar(Long id) throws DocumentoNotFoundException {
        Documento documento = buscar(id);

        try {
            Path caminho = Paths.get(documento.getPath());
            if (Files.exists(caminho)) {
                Files.delete(caminho);
                log.info("Arquivo físico removido: {}", documento.getPath());
            }
        } catch (IOException e) {
            log.warn("Não foi possível remover o arquivo físico: {}", documento.getPath());
        }

        documentoRepository.delete(documento);
        log.info("Documento removido do banco de dados: {}", id);
    }

    private void validarTipoArquivo(MultipartFile arquivo) {
        if (arquivo == null || arquivo.isEmpty()) {
            throw new IllegalArgumentException("Arquivo não pode ser vazio");
        }

        if (!tiposPermitidos.contains(arquivo.getContentType())) {
            throw new IllegalArgumentException(
                    "Tipo de arquivo não permitido: " + arquivo.getContentType() +
                    ". Tipos permitidos: " + String.join(", ", tiposPermitidos)
            );
        }
    }
}
