package br.edu.ufape.sguPraeService.servicos.interfaces;

import br.edu.ufape.sguPraeService.comunicacao.dto.auth.TokenResponse;
import br.edu.ufape.sguPraeService.exceptions.auth.KeycloakAuthenticationException;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.List;
import java.util.UUID;

public interface KeycloakService {
    void init();
    TokenResponse login(String email, String password) throws KeycloakAuthenticationException;
    TokenResponse refreshToken(String refreshToken);
    void logout(String accessToken, String refreshToken);
    UUID createUser(String email, String password, String role) throws KeycloakAuthenticationException;
    void addRoleToUser(String userId, String role);
    void deleteUser(String userId);
    String getUserId(String username);
    void resetPassword(String email);
    List<UserRepresentation> listUnverifiedUsers();
    List<String> getUserRoles(String userId);
    List<UUID> obterUsuariosPorRole(String roleName);
}