package br.edu.ufape.sguPraeService.comunicacao.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TokenResponse {
    private String access_token;
    private String refresh_token;
    private String expires_in;
    private String token_type;
    private String scope;
    private List<String> roles;
}