package br.edu.ufape.sguPraeService.servicos.interfaces;

import br.edu.ufape.sguPraeService.exceptions.notFoundExceptions.DocumentoNotFoundException;
import br.edu.ufape.sguPraeService.models.Documento;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface DocumentoService {

    /**
     * Salva um documento no sistema de arquivos e persiste os metadados no banco de dados
     * @param arquivo O arquivo a ser salvo
     * @return O documento salvo com metadados persistidos
     */
    Documento salvar(MultipartFile arquivo);

    /**
     * Busca um documento pelo ID (apenas documentos do usuário logado)
     * @param id ID do documento
     * @return O documento encontrado
     * @throws DocumentoNotFoundException se o documento não for encontrado ou não pertencer ao usuário
     */
    Documento buscar(Long id) throws DocumentoNotFoundException;

    /**
     * Lista todos os documentos do usuário logado
     * @return Lista de documentos do usuário
     */
    List<Documento> listarPorUsuario();

    /**
     * Lista todos os documentos do sistema
     * @return Lista de todos os documentos
     */
    List<Documento> listarTodos();

    /**
     * Remove um documento do sistema de arquivos e do banco de dados (apenas se pertencer ao usuário logado)
     * @param id ID do documento a ser removido
     * @throws DocumentoNotFoundException se o documento não for encontrado ou não pertencer ao usuário
     */
    void deletar(Long id) throws DocumentoNotFoundException;
}

