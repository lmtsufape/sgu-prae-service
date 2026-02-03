package br.edu.ufape.sguPraeService.servicos.interfaces;

import br.edu.ufape.sguPraeService.exceptions.notFoundExceptions.DocumentoNotFoundException;
import br.edu.ufape.sguPraeService.models.Documento;
import org.springframework.web.multipart.MultipartFile;

public interface DocumentoService {

    /**
     * Salva um documento no sistema de arquivos e persiste os metadados no banco de dados
     * @param arquivo O arquivo a ser salvo
     * @return O documento salvo com metadados persistidos
     */
    Documento salvar(MultipartFile arquivo);

    /**
     * Busca um documento pelo ID
     * @param id ID do documento
     * @return O documento encontrado
     * @throws DocumentoNotFoundException se o documento não for encontrado
     */
    Documento buscar(Long id) throws DocumentoNotFoundException;

    /**
     * Remove um documento do sistema de arquivos e do banco de dados
     * @param id ID do documento a ser removido
     * @throws DocumentoNotFoundException se o documento não for encontrado
     */
    void deletar(Long id) throws DocumentoNotFoundException;
}

