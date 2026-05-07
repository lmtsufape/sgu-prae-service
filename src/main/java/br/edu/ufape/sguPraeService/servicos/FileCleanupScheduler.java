package br.edu.ufape.sguPraeService.servicos;

import br.edu.ufape.sguPraeService.dados.DocumentoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

@Slf4j
@Component
@RequiredArgsConstructor
public class FileCleanupScheduler {

    private final DocumentoRepository documentoRepository;

    @Value("${arquivo.diretorio-upload}")
    private String uploadDir;

    /**
     * Roda todo domingo às 03:00 da manhã.
     * Expressão Cron: Segundo(0) Minuto(0) Hora(3) DiaDoMes(*) Mês(*) DiaDaSemana(SUN)
     */
    @Scheduled(cron = "0 0 3 * * SUN")
    public void varreduraSupremaDeArquivosFisicos() {
        log.info("Iniciando varredura no diretório de uploads...");

        Path diretorioBase = Paths.get(uploadDir);

        if (!Files.exists(diretorioBase) || !Files.isDirectory(diretorioBase)) {
            log.warn("Diretório de upload não encontrado: {}. Abortando.", uploadDir);
            return;
        }

        int arquivosAnalisados = 0;
        int lixosRemovidos = 0;

        try (Stream<Path> paths = Files.walk(diretorioBase)) {
            // Filtra para pegar apenas arquivos reais (ignora subpastas)
            for (Path filePath : paths.filter(Files::isRegularFile).toList()) {
                arquivosAnalisados++;
                String nomeDoArquivo = filePath.getFileName().toString();

                // Pergunta ao banco se esse arquivo tem dono
                boolean isEssencial = documentoRepository.isArquivoEssencialParaOSistema(nomeDoArquivo);

                if (!isEssencial) {
                    try {
                        Files.delete(filePath);
                        lixosRemovidos++;
                        log.debug("Arquivo órfão/inativo removido do disco: {}", nomeDoArquivo);
                    } catch (IOException e) {
                        log.error("Falha ao tentar apagar o arquivo: {}", nomeDoArquivo, e);
                    }
                }
            }
        } catch (IOException e) {
            log.error("Erro crítico ao ler o diretório de uploads.", e);
        }

        log.info("Varredura concluída. Arquivos analisados: {}. Arquivos removidos: {}", arquivosAnalisados, lixosRemovidos);
    }
}