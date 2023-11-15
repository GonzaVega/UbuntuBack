package semillero.ubuntu.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.client.OAuth2LoginConfigurer;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(auth -> {
            auth.requestMatchers("/**").permitAll();
                    //auth.requestMatchers("/admin").hasRole("ADMIN");
                    //auth.requestMatchers("/user").hasAnyRole("USER","ADMIN");
            auth.anyRequest().authenticated();
        })
                //.oauth2Login(withDefaults())
                .oauth2Login(OAuth2LoginConfigurer::disable)  // Deshabilita completamente la autenticación OAuth2
                .formLogin(withDefaults())
                .formLogin(login -> login.disable())
                .build();
    }
}
