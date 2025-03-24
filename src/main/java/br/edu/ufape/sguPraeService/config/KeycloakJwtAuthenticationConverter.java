package br.edu.ufape.sguPraeService.config;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;




@Component
public class KeycloakJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final JwtGrantedAuthoritiesConverter defaultGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public AbstractAuthenticationToken convert(@NonNull Jwt jwt) {
        // Junta as autoridades do token padrão com as autoridades extraídas do Keycloak
        Collection<GrantedAuthority> authorities = Stream
                .concat(defaultGrantedAuthoritiesConverter.convert(jwt).stream(),
                        extractAuthorities(jwt).stream())
                .collect(Collectors.toSet());

        return new JwtAuthenticationToken(jwt, authorities, jwt.getClaimAsString("preferred_username"));
    }

    private Collection<GrantedAuthority> extractAuthorities(Jwt jwt) {
        Set<String> rolesWithPrefix = new HashSet<>();
        rolesWithPrefix.addAll(getRealmRoles(jwt));   // Pega os roles do realm
        rolesWithPrefix.addAll(getResourceRoles(jwt)); // Pega os roles dos clients (recursos)
        return AuthorityUtils.createAuthorityList(rolesWithPrefix.toArray(new String[0]));
    }

    private Set<String> getRealmRoles(Jwt jwt) {
        Set<String> rolesWithPrefix = new HashSet<>();
        JsonNode json = objectMapper.convertValue(jwt.getClaim("realm_access"), JsonNode.class);
        if (json != null && json.has("roles")) {
            json.get("roles").forEach(role -> rolesWithPrefix.add("ROLE_" + role.asText().toUpperCase()));
        }
        return rolesWithPrefix;
    }

    private Set<String> getResourceRoles(Jwt jwt) {
        Set<String> rolesWithPrefix = new HashSet<>();
        JsonNode resourceAccess = objectMapper.convertValue(jwt.getClaim("resource_access"), JsonNode.class);
        if (resourceAccess != null) {
            resourceAccess.fields().forEachRemaining(clientEntry -> {
                JsonNode roles = clientEntry.getValue().get("roles");
                if (roles != null) {
                    roles.forEach(role -> rolesWithPrefix.add("ROLE_" + role.asText().toUpperCase()));
                }
            });
        }
        return rolesWithPrefix;
    }
}