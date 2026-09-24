package br.edu.ufape.sguPraeService.config.filters;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Component
public class TokenResponseRewriteFilter extends OncePerRequestFilter {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();
        String method = request.getMethod();

        HttpServletRequest requestToUse = request;

        // 1. Injetar Authorization Header se houver cookie 'accessToken' (Aplica para todas as rotas restritas)
        String accessToken = getCookieValue(request, "accessToken");
        if (accessToken != null && request.getHeader(HttpHeaders.AUTHORIZATION) == null) {
            requestToUse = new HeaderModifierRequestWrapper(requestToUse);
            ((HeaderModifierRequestWrapper) requestToUse).addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
        }

        // 2. Fluxo de Refresh: Injetar cookie refreshToken como parâmetro
        if ("POST".equalsIgnoreCase(method) && path.matches(".*/refresh$")) {
            String refreshToken = getCookieValue(request, "refreshToken");
            if (refreshToken != null) {
                if (!(requestToUse instanceof HeaderModifierRequestWrapper)) {
                    requestToUse = new HeaderModifierRequestWrapper(requestToUse);
                }
                ((HeaderModifierRequestWrapper) requestToUse).addParameter("refresh_token", refreshToken);
                ((HeaderModifierRequestWrapper) requestToUse).addParameter("grant_type", "refresh_token");
            }
        }

        // 3. Fluxo de Logout: Injetar refreshToken como parâmetro
        if ("POST".equalsIgnoreCase(method) && path.matches(".*/logout$")) {
            String refreshToken = getCookieValue(request, "refreshToken");
            if (refreshToken != null) {
                if (!(requestToUse instanceof HeaderModifierRequestWrapper)) {
                    requestToUse = new HeaderModifierRequestWrapper(requestToUse);
                }
                ((HeaderModifierRequestWrapper) requestToUse).addParameter("refresh_token", refreshToken);
            }
        }

        // Preparar para interceptar a resposta do Controller
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

        // Continua o fluxo chamando o AuthController
        filterChain.doFilter(requestToUse, responseWrapper);

        // Pós-processamento: Reescrever a resposta final baseada na rota
        if ("POST".equalsIgnoreCase(method) && (path.matches(".*/login$") || path.matches(".*/refresh$"))) {
            processTokenResponse(responseWrapper);
        } else if ("POST".equalsIgnoreCase(method) && path.matches(".*/logout$")) {
            processLogoutResponse(responseWrapper);
        }

        // Escreve a resposta interceptada e modificada de volta para o cliente HTTP
        responseWrapper.copyBodyToResponse();
    }

    private void processTokenResponse(ContentCachingResponseWrapper responseWrapper) throws IOException {
        byte[] responseArray = responseWrapper.getContentAsByteArray();
        if (responseArray.length > 0) {
            try {
                JsonNode root = MAPPER.readTree(responseArray);
                String access = root.path("access_token").asText(null);
                String refresh = root.path("refresh_token").asText(null);

                if (access != null && refresh != null) {
                    // Adiciona cookies seguros na resposta
                    addCookie(responseWrapper, "accessToken", access, 15 * 60);
                    addCookie(responseWrapper, "refreshToken", refresh, 7 * 24 * 3600);

                    Long exp = extractExpFromToken(access);
                    String successResponse = String.format(
                            "{\"message\":\"Login bem-sucedido\",\"exp\":%d}",
                            exp != null ? exp : (System.currentTimeMillis() / 1000) + 900
                    );

                    // Reescreve o corpo da resposta escondendo o token (Exatamente como o Gateway fazia)
                    byte[] newBody = successResponse.getBytes(StandardCharsets.UTF_8);
                    responseWrapper.resetBuffer();
                    responseWrapper.getOutputStream().write(newBody);
                    responseWrapper.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    responseWrapper.setContentLength(newBody.length);
                }
            } catch (Exception ignored) {
                // Se der erro no parse, mantém a resposta de erro original
            }
        }
    }

    private void processLogoutResponse(ContentCachingResponseWrapper responseWrapper) {
        addCookie(responseWrapper, "accessToken", "", 0);
        addCookie(responseWrapper, "refreshToken", "", 0);
    }

    private void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
        // Criando cabeçalho manualmente para suportar as flags SameSite=Lax nativamente
        String cookieHeader = String.format("%s=%s; Max-Age=%d; Path=/; HttpOnly; SameSite=Lax", name, value, maxAge);
        response.addHeader(HttpHeaders.SET_COOKIE, cookieHeader);
    }

    private String getCookieValue(HttpServletRequest request, String name) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (name.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    private Long extractExpFromToken(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length != 3) return null;
            byte[] payloadBytes = java.util.Base64.getUrlDecoder().decode(parts[1]);
            JsonNode payloadJson = MAPPER.readTree(new String(payloadBytes, StandardCharsets.UTF_8));
            return payloadJson.path("exp").asLong();
        } catch (Exception e) {
            return null;
        }
    }

    // Classe Wrapper interna para forjar novos headers e parâmetros na requisição (Necessário para a API de Servlets)
    private static class HeaderModifierRequestWrapper extends HttpServletRequestWrapper {
        private final Map<String, String> customHeaders;
        private final Map<String, String[]> customParameters;

        public HeaderModifierRequestWrapper(HttpServletRequest request) {
            super(request);
            this.customHeaders = new HashMap<>();
            this.customParameters = new HashMap<>(request.getParameterMap());
        }

        public void addHeader(String name, String value) {
            this.customHeaders.put(name, value);
        }

        public void addParameter(String name, String value) {
            this.customParameters.put(name, new String[]{value});
        }

        @Override
        public String getHeader(String name) {
            String headerValue = customHeaders.get(name);
            return (headerValue != null) ? headerValue : super.getHeader(name);
        }

        @Override
        public Enumeration<String> getHeaderNames() {
            Set<String> set = new HashSet<>(customHeaders.keySet());
            Enumeration<String> e = super.getHeaderNames();
            while (e.hasMoreElements()) {
                set.add(e.nextElement());
            }
            return Collections.enumeration(set);
        }

        @Override
        public String getParameter(String name) {
            String[] values = customParameters.get(name);
            return (values != null && values.length > 0) ? values[0] : null;
        }

        @Override
        public Map<String, String[]> getParameterMap() {
            return Collections.unmodifiableMap(customParameters);
        }

        @Override
        public Enumeration<String> getParameterNames() {
            return Collections.enumeration(customParameters.keySet());
        }

        @Override
        public String[] getParameterValues(String name) {
            return customParameters.get(name);
        }
    }
}