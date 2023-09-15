package com.paymybuddy.app.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

/**
 * Configuration class for setting up web security in the application.
 *
 * @Configuration
 * @EnableWebSecurity
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    /**
     * Defines a bean for password encoding.
     *
     * @return A BCryptPasswordEncoder bean.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configures security filter chains for different HTTP requests and authentication methods.
     *
     * @param http The HttpSecurity object to configure security.
     * @return A SecurityFilterChain bean.
     * @throws Exception If an exception occurs during configuration.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests((requests) -> requests
                .requestMatchers("/", "/home", "/login/oauth2", "/contact", "/register", "/js/**", "/sign", "/css/**",
                        "/favIcon.png")
                .permitAll()
                .requestMatchers("/profile", "/transfer", "/transactions/create", "/add-relation")
                .authenticated()
                .requestMatchers("/admin/**")
                .hasAuthority("ADMIN")
                .anyRequest()
                .permitAll())
                .formLogin((form) -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/transfer")
                        .permitAll())
                .logout((logout) -> logout
                        .permitAll())
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/login/oauth2"));

        http.csrf()
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());

        return http.build();
    }
}
