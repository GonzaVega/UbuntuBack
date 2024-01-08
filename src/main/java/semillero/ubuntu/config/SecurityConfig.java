package semillero.ubuntu.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.client.OAuth2LoginConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import semillero.ubuntu.utils.filters.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Value("${web.cors.allowed-origins}")
    private String corsAllowedOrigins;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    RequestMatcher adminUrls = new OrRequestMatcher(
            Arrays.asList(
                    new AntPathRequestMatcher("/api/v1/publication/**"),
                    new AntPathRequestMatcher("/api/v1/category/**"),
                    new AntPathRequestMatcher("/api/v1/microentrepreneurship/**"),
                    new AntPathRequestMatcher("/api/v1/message/**")
            )
    );

    RequestMatcher publicUrls = new OrRequestMatcher(
            new AntPathRequestMatcher("/api/v1/auth/**"),
            new AntPathRequestMatcher("/api/v1/geo/**"),
            new AntPathRequestMatcher("/api/v1/message/**","POST"),
            new AntPathRequestMatcher("/api/v1/publication/activas","GET"),
            new AntPathRequestMatcher("/api/v1/publication/{id}","GET"),
            new AntPathRequestMatcher("/api/v1/publication/ultimas10","GET"),
            new AntPathRequestMatcher("/api/v1/category/**","GET"),
            new AntPathRequestMatcher("/api/v1/microentrepreneurship/all","GET"),
            new AntPathRequestMatcher("/api/v1/microentrepreneurship/{id}","GET"),
            new AntPathRequestMatcher("/api/v1/microentrepreneurship/find/**","GET"),
            new AntPathRequestMatcher("/api/v1/microentrepreneurship/find/category/**","GET"),
            new AntPathRequestMatcher("/api/v1/microentrepreneurship/count/categories","GET")
            );



    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
         http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                                .requestMatchers(HttpMethod.OPTIONS).permitAll()
                                .requestMatchers(publicUrls).permitAll()
                                .requestMatchers(adminUrls).hasAuthority("ADMIN")
                        .anyRequest().authenticated()
                )
                .cors(cors -> cors
                        .configurationSource(corsConfigurationSource())
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

         return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(corsAllowedOrigins.split(",")));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS", "HEAD"));
        configuration.setAllowedHeaders(Arrays.asList("Content-Type", "Authorization", "Requestor-Type", "X-Get-Reader"));
        configuration.setExposedHeaders(Arrays.asList("Content-Type", "Authorization", "Requestor-Type", "X-Get-Reader"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

}
