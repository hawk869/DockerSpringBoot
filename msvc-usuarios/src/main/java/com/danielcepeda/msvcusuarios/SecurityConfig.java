package com.danielcepeda.msvcusuarios;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        httpSecurity.authorizeRequests()
                .antMatchers("/authorized").permitAll()
                .antMatchers(HttpMethod.GET, "/", "/{id}")
                .hasAnyAuthority("SCOPE_read", "SCOPE_write")
                .antMatchers(HttpMethod.POST, "/").hasAuthority("SCOPE_write")
                .antMatchers(HttpMethod.DELETE, "/{id}").hasAuthority("SCOPE_write")
                .antMatchers(HttpMethod.DELETE, "/{id}").hasAuthority("SCOPE_write")
                .anyRequest().authenticated()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .oauth2Login(oauth2Login -> oauth2Login.loginPage("/oauth2/authorization/msvc-usuarios-client"))
                .oauth2Client(Customizer.withDefaults())
                .oauth2ResourceServer().jwt();
        return httpSecurity.build();
    }
}
