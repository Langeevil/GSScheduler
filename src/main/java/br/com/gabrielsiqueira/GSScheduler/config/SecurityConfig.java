package br.com.gabrielsiqueira.GSScheduler.config;

import br.com.gabrielsiqueira.GSScheduler.service.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.session.HttpSessionEventPublisher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .userDetailsService(userDetailsService)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/login",
                                "/css/**",
                                "/js/**",
                                "/img/**",
                        "/webjars/**"
                    ).permitAll()
                    .requestMatchers("/", "/home", "/register").permitAll()
                    .requestMatchers("/api/admin/**").hasRole("ADMIN")
                    .requestMatchers("/admin/**").hasRole("ADMIN")
                    .requestMatchers("/api/tasks/**").authenticated()
                    .requestMatchers("/api/settings/**").authenticated()
                    .requestMatchers("/api/account/**").authenticated()
                    .requestMatchers("/api/reports/**").authenticated()
                    .anyRequest().authenticated()
            )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                        .sessionConcurrency(concurrency -> concurrency
                                .maximumSessions(10)
                                .sessionRegistry(sessionRegistry())
                        )
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/dashboard", true)
                        .failureUrl("/login?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout=true")
                        .permitAll()
                )
                .exceptionHandling(ex -> ex.accessDeniedHandler((request, response, accessDeniedException) ->
                        response.sendRedirect(request.getContextPath() + "/access-denied?from=" + request.getRequestURI())
                ))
                .csrf(csrf -> csrf.disable()); // deixa desativado só em dev

        return http.build();
    }
}
