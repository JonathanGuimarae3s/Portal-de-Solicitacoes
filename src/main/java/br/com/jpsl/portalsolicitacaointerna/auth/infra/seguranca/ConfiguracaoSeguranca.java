package br.com.jpsl.portalsolicitacaointerna.auth.infra.seguranca;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class ConfiguracaoSeguranca {

    private final FiltroSeguranca filtroSeguranca;

    public ConfiguracaoSeguranca(FiltroSeguranca filtroSeguranca) {
        this.filtroSeguranca = filtroSeguranca;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http.csrf(csrf -> csrf.disable())
                .sessionManagement(
                        sessionManagement ->
                                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(
                        authorize -> authorize.
                                requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                                .requestMatchers(HttpMethod.POST, "/usuarios").permitAll()

                                .requestMatchers(HttpMethod.GET, "/tipos", "/tipos/**")
                                .hasAnyRole("ADMIN", "GESTOR", "USUARIO")
                                .requestMatchers(HttpMethod.POST, "/tipos").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/tipos/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/tipos/**").hasRole("ADMIN")

                                .requestMatchers(HttpMethod.GET, "/usuarios").hasAnyRole("ADMIN", "GESTOR")
                                .requestMatchers(HttpMethod.PUT, "/usuarios/**").hasRole("ADMIN")

                                .requestMatchers(HttpMethod.GET, "/solicitacoes", "/solicitacoes/**")
                                .hasAnyRole("ADMIN", "GESTOR", "USUARIO")
                                .requestMatchers(HttpMethod.POST, "/solicitacoes")
                                .hasAnyRole("ADMIN", "GESTOR", "USUARIO")
                                .requestMatchers(HttpMethod.PUT, "/solicitacoes/**")
                                .hasAnyRole("ADMIN", "GESTOR", "USUARIO")

                                .anyRequest().authenticated()
                )
                .addFilterBefore(filtroSeguranca, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

  

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
