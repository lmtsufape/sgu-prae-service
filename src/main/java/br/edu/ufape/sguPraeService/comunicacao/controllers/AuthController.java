package br.edu.ufape.sguPraeService.comunicacao.controllers;

import br.edu.ufape.sguPraeService.comunicacao.dto.auth.TokenResponse;
import br.edu.ufape.sguPraeService.fachada.Fachada;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final Fachada fachada;

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(
            @RequestParam("email") String username,
            @RequestParam("senha") String password,
            HttpServletResponse response) {

        TokenResponse tokenData = fachada.login(username, password);

        // 1. Embutindo o Refresh Token no Cookie (Lógica trazida do antigo Gateway)
        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", tokenData.getRefresh_token())
                .httpOnly(true)
                .secure(true) // Mude para false se testar em localhost sem HTTPS
                .path("/")
                .maxAge(7 * 24 * 3600) // 7 dias
                .sameSite("Lax")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

        // 2. Embutindo o Access Token no Cookie
        ResponseCookie accessCookie = ResponseCookie.from("accessToken", tokenData.getAccess_token())
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(15 * 60) // 15 minutos
                .sameSite("Lax")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());

        return ResponseEntity.ok(tokenData);
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refresh(
            @CookieValue(name = "refreshToken", required = false) String cookieRefreshToken,
            @RequestParam(value = "refresh_token", required = false) String paramRefreshToken,
            HttpServletResponse response) {

        // Pega o token do cookie (automático do browser) ou do parâmetro (fallback)
        String refreshToken = (cookieRefreshToken != null) ? cookieRefreshToken : paramRefreshToken;

        TokenResponse tokenData = fachada.refresh(refreshToken);

        // Renova os Cookies na resposta
        ResponseCookie newRefreshCookie = ResponseCookie.from("refreshToken", tokenData.getRefresh_token())
                .httpOnly(true).secure(true).path("/").maxAge(7 * 24 * 3600).sameSite("Lax").build();
        response.addHeader(HttpHeaders.SET_COOKIE, newRefreshCookie.toString());

        ResponseCookie newAccessCookie = ResponseCookie.from("accessToken", tokenData.getAccess_token())
                .httpOnly(true).secure(true).path("/").maxAge(15 * 60).sameSite("Lax").build();
        response.addHeader(HttpHeaders.SET_COOKIE, newAccessCookie.toString());

        return ResponseEntity.ok(tokenData);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @CookieValue(name = "accessToken", required = false) String cookieAccessToken,
            @CookieValue(name = "refreshToken", required = false) String cookieRefreshToken,
            @RequestHeader(value = "Authorization", required = false) String headerToken,
            @RequestParam(value = "refresh_token", required = false) String paramRefreshToken,
            HttpServletResponse response) {

        String accessToken = (cookieAccessToken != null) ? cookieAccessToken : ((headerToken != null) ? headerToken.replace("Bearer ", "") : "");
        String refreshToken = (cookieRefreshToken != null) ? cookieRefreshToken : paramRefreshToken;

        fachada.logout(accessToken, refreshToken);

        // Limpa (Invalida) os cookies no navegador do usuário configurando maxAge(0)
        ResponseCookie cleanRefreshCookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true).secure(true).path("/").maxAge(0).sameSite("Lax").build();
        response.addHeader(HttpHeaders.SET_COOKIE, cleanRefreshCookie.toString());

        ResponseCookie cleanAccessCookie = ResponseCookie.from("accessToken", "")
                .httpOnly(true).secure(true).path("/").maxAge(0).sameSite("Lax").build();
        response.addHeader(HttpHeaders.SET_COOKIE, cleanAccessCookie.toString());

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(@RequestParam("email") String email) {
        fachada.resetPassword(email);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/roles")
    public ResponseEntity<List<String>> getUserRoles(){
        List<String> roles = fachada.getUserRoles();
        return ResponseEntity.ok(roles);
    }
}