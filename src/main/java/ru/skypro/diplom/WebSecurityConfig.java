package ru.skypro.diplom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import ru.skypro.diplom.service.AuthService;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class WebSecurityConfig {

    private final Logger log = LoggerFactory.getLogger(this.getClass());


    private static final String[] AUTH_WHITELIST = {
            "/swagger-resources/**",
            "/swagger-ui.html",
            "/v3/api-docs",
            "/webjars/**",
            "/ads",
            "/ads/*/image",
            "/login", "/register"
    };

    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        log.info("userDetailsService");
        UserDetails userDetails = User.withDefaultPasswordEncoder()
                .username("Q1@ya.ru")
                .password("qqqqqqqq")
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(userDetails);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        log.info("filterChain");
        http
                .csrf().disable()
                .authorizeHttpRequests((authz) ->
                                authz.mvcMatchers(AUTH_WHITELIST).permitAll()
                                .mvcMatchers("/ads/**", "/users/**").authenticated()
                )
                .cors().and()
                .httpBasic(withDefaults());
        return http.build();
    }


}
