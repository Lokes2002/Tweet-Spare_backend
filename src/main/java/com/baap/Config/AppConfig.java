package com.baap.Config;

import java.util.Arrays;
import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import jakarta.servlet.http.HttpServletRequest;



@Configuration
@EnableWebSecurity
public class AppConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests(Authorize -> Authorize.requestMatchers("/api/**").authenticated()
                    .anyRequest().permitAll()
                ).addFilterBefore(new JwtTokenValidator(), BasicAuthenticationFilter.class)
                .csrf().disable()
                .cors().configurationSource(corsConfigurationSource()).and()
                .httpBasic().and().formLogin();
        return http.build();
    }

    private CorsConfigurationSource corsConfigurationSource() {
    	
    	return new CorsConfigurationSource() {
    		
    	@Override
    	public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
    		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    		 CorsConfiguration config = new CorsConfiguration();
    	        config.setAllowedOrigins(Arrays.asList("http://localhost:3000")); // Frontend ka URL yahan specify karein
    	        config.setAllowedMethods(Arrays.asList("*")); // Sabhi HTTP methods allow karein
    	        config.setAllowedHeaders(Arrays.asList("*")); // Sabhi headers allow karein
    	        config.setExposedHeaders(Arrays.asList("Authorization")); // Additional headers jo expose kiye jayenge
    	        config.setAllowCredentials(true); // Credentials (cookies, authentication headers) allow karein
    	        source.registerCorsConfiguration("/**", config);

   
        return config;
    }
    	
    	};
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
  