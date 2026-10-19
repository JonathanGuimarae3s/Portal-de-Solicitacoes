package br.com.jpsl.portalsolicitacaointerna.auth.infra.seguranca;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class ConfiguracaoSeguranca {

    public static final String[] USER_ADMIN_GESTOR = {"ADMIN", "GESTOR", "USUARIO"};
    public static final String[] GESTOR_ADMIN = {"ADMIN", "GESTOR"};
    public static final String USUARIO = "USUARIO";
    public static final String GESTOR = "GESTOR";
    public static final String ADMIN = "ADMIN";
    private final FiltroSeguranca filtroSeguranca;

    public ConfiguracaoSeguranca(FiltroSeguranca filtroSeguranca) {
        this.filtroSeguranca = filtroSeguranca;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {

        return http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(
                        sessionManagement ->
                                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(
                        authorize -> authorize.
                                requestMatchers(HttpMethod.POST, "/auth/login")
                                .permitAll()
                                .requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**")
                                .permitAll()

                                .requestMatchers(HttpMethod.POST, "/usuarios")
                                .hasAnyRole(GESTOR_ADMIN)

                                .requestMatchers(HttpMethod.GET, "/tipos", "/tipos/**")
                                .hasAnyRole(USER_ADMIN_GESTOR)
                                .requestMatchers(HttpMethod.POST, "/tipos")
                                .hasRole(ADMIN)
                                .requestMatchers(HttpMethod.PUT, "/tipos/**")
                                .hasRole(ADMIN)
                                .requestMatchers(HttpMethod.DELETE, "/tipos/**")
                                .hasRole(ADMIN)

                                .requestMatchers(HttpMethod.GET, "/dashboard", "/dashboard/**")
                                .hasAnyRole(USER_ADMIN_GESTOR)

                                .requestMatchers(HttpMethod.GET, "/usuarios")
                                .hasAnyRole(GESTOR_ADMIN)
                                .requestMatchers(HttpMethod.GET, "/usuarios/{id}")
                                .hasAnyRole(USER_ADMIN_GESTOR)
                                .requestMatchers(HttpMethod.PUT, "/usuarios/**")
                                .hasRole(ADMIN)
                                .requestMatchers(HttpMethod.GET, "/solicitacoes", "/solicitacoes/**")
                                .hasAnyRole(USER_ADMIN_GESTOR)

                                .requestMatchers(HttpMethod.POST, "/solicitacoes")
                                .hasAnyRole(USER_ADMIN_GESTOR)
                                .requestMatchers(HttpMethod.PUT, "/solicitacoes/**")
                                .hasAnyRole(USER_ADMIN_GESTOR)

                                .requestMatchers(HttpMethod.PATCH, "/usuarios/**")
                                .hasRole(ADMIN)

                                .anyRequest().authenticated()
                )
                .addFilterBefore(filtroSeguranca, UsernamePasswordAuthenticationFilter.class)
                .build();
    }


    //https://www.youtube.com/watch?v=Fha6Il-5RYE
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "X-Requested-With"));
        configuration.setExposedHeaders(Arrays.asList("Authorization"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
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
