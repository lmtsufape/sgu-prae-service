package br.edu.ufape.sguPraeService.config;

import br.edu.ufape.sguPraeService.fachada.Fachada;
import org.springframework.beans.factory.annotation.Value;
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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.UUID;

@EnableWebSecurity
@Configuration
public class WebConfig {

    private final Fachada fachada;

    // Resgata as URLs do frontend permitidas a partir do application.yml
    @Value("${common.front}")
    private String allowedOrigins;

    public WebConfig(Fachada fachada) {
        this.fachada = fachada;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // <-- CORS absorvido do Gateway
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
                        .requestMatchers("/public/**").permitAll() // <-- Rota pública genérica trazida do Gateway

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

    /**
     * Configuração de CORS extraída da antiga GatewaySecurityConfig.
     * Fundamental para que o Frontend consiga ler/enviar Cookies e realizar chamadas.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Permite as origens configuradas no application.yml (ex: http://localhost:3000)
        configuration.setAllowedOrigins(Arrays.asList(allowedOrigins.split(",")));

        // Permite os métodos HTTP comuns
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));

        // Permite o tráfego de cabeçalhos de autenticação e controle
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));

        // CRÍTICO: allowCredentials(true) é obrigatório se quisermos usar Cookies (HttpOnly) para os Tokens
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // Aplica essa regra a todas as rotas do Monolito
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}