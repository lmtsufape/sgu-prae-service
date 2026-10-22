package br.edu.ufape.sguPraeService.auth;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

@Component
public class AuthenticatedUserProvider {

    public UUID getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof JwtAuthenticationToken jwtToken) {

            // 1. Imprime o payload real do Token no console (debug)
            Map<String, Object> claims = jwtToken.getToken().getClaims();
            System.out.println("\n====== DEBUG: CLAIMS DO JWT ======");
            claims.forEach((chave, valor) -> System.out.println("Chave: '" + chave + "' | Valor: " + valor));
            System.out.println("==================================\n");

            // 2. Tenta pegar pelas vias nativas
            String subject = jwtToken.getToken().getSubject();
            if (subject == null) {
                subject = jwtToken.getToken().getClaimAsString("sub");
            }

            // 3. Fallbacks para claims customizadas (comuns em ambientes refatorados)
            if (subject == null) subject = jwtToken.getToken().getClaimAsString("user_id");
            if (subject == null) subject = jwtToken.getToken().getClaimAsString("id");
            if (subject == null) subject = jwtToken.getToken().getClaimAsString("uid");

            // 4. Trava final
            if (subject == null) {
                throw new IllegalStateException("ID não encontrado. Verifique os logs do console ('DEBUG: CLAIMS DO JWT') para descobrir qual chave está guardando o UUID do usuário.");
            }

            try {
                return UUID.fromString(subject);
            } catch (IllegalArgumentException e) {
                throw new IllegalStateException("A chave encontrada no JWT não é um UUID válido: " + subject);
            }
        }

        throw new IllegalStateException("Usuário não autenticado via JWT.");
    }
}