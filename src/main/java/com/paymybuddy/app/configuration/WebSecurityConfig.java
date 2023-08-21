package com.paymybuddy.app.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
        .authorizeHttpRequests((requests) -> requests
            .requestMatchers("/", "/home","/login/oauth2", "/contact", "/register", "/sign", "/css/**", "/favIcon.png").permitAll()
            .requestMatchers("/profile", "/transfer","/transactions/create", "/add-relation").authenticated()
            .anyRequest().permitAll())
            .formLogin((form) -> form.loginPage("/login").permitAll())
            .logout((logout) -> logout.permitAll())
            .oauth2Login(oauth2 -> oauth2
    			    .loginPage("/login/oauth2"))
            
            ;

        return http.build();
    }

	
	 
	
}