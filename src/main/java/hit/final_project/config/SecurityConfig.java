// src/main/java/hit/final_project/config/SecurityConfig.java
package hit.final_project.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/**").permitAll()  // Allow all requests without login
                )
                .csrf((csrf) -> csrf.disable())  // Disable CSRF for simplicity (consider enabling in production)
                .headers((headers) -> headers.frameOptions((frameOptions) -> frameOptions.disable()));  // Enable H2 console access

        return http.build();
    }
}


// THIS IS NOT IN USE