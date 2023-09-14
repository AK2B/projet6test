package com.paymybuddy.app.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		http.authorizeHttpRequests((requests) -> requests

				.requestMatchers("/", "/home", "/login/oauth2", "/contact", "/register", "/js/**", "/sign", "/css/**",
						"/favIcon.png")
				.permitAll().requestMatchers("/profile", "/transfer", "/transactions/create", "/add-relation")
				.authenticated().requestMatchers("/admin/**").hasAuthority("ADMIN").anyRequest().permitAll())

				.formLogin((form) -> form.loginPage("/login").defaultSuccessUrl("/transfer").permitAll())
				.logout((logout) -> logout.permitAll()).oauth2Login(oauth2 -> oauth2.loginPage("/login/oauth2"));

		 http
         .csrf()
         .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
		return http.build();
	}

}