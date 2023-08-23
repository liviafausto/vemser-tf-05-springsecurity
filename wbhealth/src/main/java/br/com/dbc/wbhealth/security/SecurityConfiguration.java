package br.com.dbc.wbhealth.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final TokenService tokenService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        final String ADMIN = "ADMIN";
        final String PACIENTE = "PACIENTE";
        final String RECEPCAO = "RECEPCAO";

        http.headers().frameOptions().disable()
                .and().cors()
                .and().csrf().disable()
                .authorizeHttpRequests((authz) -> authz
                        .antMatchers("/", "/auth", "/auth/login").permitAll()
                        .antMatchers("/auth/create-user").hasRole(ADMIN)
                        .antMatchers(HttpMethod.GET, "/atendimento/paciente/**").hasAnyRole(PACIENTE, RECEPCAO, ADMIN)
                        .antMatchers(HttpMethod.GET, "/medico/**").hasAnyRole(RECEPCAO, ADMIN)
                        .antMatchers(HttpMethod.GET, "/hospital/**").hasAnyRole(RECEPCAO, ADMIN)
                        .antMatchers("/atendimento/**", "/paciente/**").hasAnyRole(RECEPCAO, ADMIN)
                        .antMatchers("/**").hasRole(ADMIN)
                        .anyRequest().authenticated()
                );
        http.addFilterBefore(
                new TokenAuthenticationFilter(tokenService),
                UsernamePasswordAuthenticationFilter.class
        );

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().antMatchers(
                "/v3/api-docs",
                "/v3/api-docs/**",
                "/swagger-resources/**",
                "/swagger-ui/**"
        );
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedMethods("*")
                        .exposedHeaders("Authorization");
            }
        };
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new Argon2PasswordEncoder();
    }
}
