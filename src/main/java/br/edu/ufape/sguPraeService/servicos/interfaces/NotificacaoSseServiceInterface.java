package br.edu.ufape.sguPraeService.servicos.interfaces;

import br.edu.ufape.sguPraeService.comunicacao.mensageria.NotificacaoEvent;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.UUID;

public interface NotificacaoSseServiceInterface {
    SseEmitter subscrever(UUID userId);
    void emitirSinalDeNovaNotificacao(UUID userId);
    void removerTodosEmittersDoUsuario(UUID userId); // Mantemos para o Logout
}