package br.edu.ufape.sguPraeService.config;

import br.edu.ufape.sguPraeService.fachada.Fachada;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import java.util.UUID;

@EnableWebSecurity
@Configuration
public class WebConfig {

    private final Fachada fachada;

    public WebConfig(Fachada fachada) {
        this.fachada = fachada;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .addLogoutHandler((request, response, authentication) -> {
                            if (authentication != null && authentication.getPrincipal() instanceof Jwt jwt) {
                                String subject = jwt.getSubject();
                                if (subject != null) {
                                    fachada.limparConexoesSse(UUID.fromString(subject)); // Limpeza mantida do Auth
                                }
                            }
                        })
                        .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler(HttpStatus.OK))
                )
                .authorizeHttpRequests(authz -> authz
                        // Rotas Abertas do Sistema Antigo
                        .requestMatchers("/api-doc/**", "/swagger-ui/**").permitAll()
                        .requestMatchers("/login", "/refresh", "/logout", "/reset-password").permitAll()

                        // Rotas de Cadastro de acordo com a Issue #85 (Estudante é público, o resto é fechado)
                        .requestMatchers(HttpMethod.POST, "/cadastro/estudante").permitAll()
                        .requestMatchers(HttpMethod.GET, "/curso").permitAll() // Necessário para o front listar cursos no cadastro do estudante

                        // Todas as outras rotas exigem token
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(auth -> auth.jwt(token ->
                        token.jwtAuthenticationConverter(new KeycloakJwtAuthenticationConverter())));

        return http.build();
    }
}