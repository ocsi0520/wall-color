package com.my_wall_color.color_manager.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic(withDefaults())
//                .oauth2ResourceServer((oauth2) -> oauth2  // authentication
//                        .jwt(withDefaults())
//                )
                .authorizeHttpRequests((authorize) -> authorize  // authorization
                        .anyRequest().authenticated()
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsManager() {
        UserDetails johnDoeUser = User
                .withUsername("jdoe").password("$2a$12$/wnuwqCoou1NwfDGzAPTFOsDgbyIblbOyGp.8WRvPMYt/GWSn8XYy")
                .build();
        //  jdoe' -- pw: user1
        return new InMemoryUserDetailsManager(johnDoeUser);
    }
}
