
package com.produtos.gerenciador.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class SecurityConfig {

    // Configura a segurança da API.
    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http
            // Ativa o CORS.
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))

            // Não guarda a sessão do usuário.
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            // Desativa o CSRF para nossa API HTTP Basic.
            .csrf(csrf -> csrf.disable())

            // Define quais rotas precisam de login.
            .authorizeHttpRequests(auth -> auth

                // Permite consultar produtos e categorias sem login.
                .requestMatchers(HttpMethod.GET,
                        "/categorias", "/categorias/**",
                        "/produtos", "/produtos/**")
                .permitAll()

                // Exige login para cadastrar.
                .requestMatchers(HttpMethod.POST,
                        "/categorias", "/produtos")
                .authenticated()

                // Exige login para atualizar.
                .requestMatchers(HttpMethod.PUT,
                        "/categorias/**", "/produtos/**")
                .authenticated()

                // Exige login para excluir.
                .requestMatchers(HttpMethod.DELETE,
                        "/categorias/**", "/produtos/**")
                .authenticated()

                // Protege as outras rotas.
                .anyRequest().authenticated()
            )

            // Ativa o login com usuário e senha.
            .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    // Protege a senha usando BCrypt.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Cria um usuário para acessar a API.
    @Bean
    public UserDetailsService userDetailsService(
            @Value("${APP_USER}") String usuario,
            @Value("${APP_PASSWORD}") String senha,
            PasswordEncoder passwordEncoder) {

        UserDetails usuarioApi = User.builder()
                .username(usuario)
                .password(passwordEncoder.encode(senha))
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(usuarioApi);
    }

    // Configura quais origens podem acessar a API.
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration config = new CorsConfiguration();

        // Permite acesso de um frontend local.
        config.setAllowedOrigins(
                List.of("http://localhost:3000"));

        // Define os métodos permitidos.
        config.setAllowedMethods(
                List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // Define os cabeçalhos permitidos.
        config.setAllowedHeaders(
                List.of("Authorization", "Content-Type"));

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        // Aplica o CORS em todas as rotas.
        source.registerCorsConfiguration("/**", config);

        return source;
    }
}
