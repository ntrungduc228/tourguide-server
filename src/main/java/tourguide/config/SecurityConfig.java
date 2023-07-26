package tourguide.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import tourguide.security.AuthEntryPointJwt;
import tourguide.security.CustomUserDetailsService;
import tourguide.security.filter.JwtFilter;
import tourguide.security.provider.JwtAuthenticationProvider;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig {
    @Autowired
    JwtFilter jwtFilter;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    // Injecting JWT custom authentication provider
    @Autowired
    JwtAuthenticationProvider jwtAuthenticationProvider;

    //New version (After Spring Security 5.7.0)
    @Bean
    AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    // Custom default prefix ROLE
    @Bean
    static GrantedAuthorityDefaults grantedAuthorityDefaults() {
        return new GrantedAuthorityDefaults("");
    }

    // adding our custom authentication providers
    // authentication manager will call these custom provider's
    // authenticate methods from now on.
    //New version (After Spring Security 5.7.0)
    @Autowired
    void registerProvider(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(jwtAuthenticationProvider);
    }

    @Bean
    public SecurityFilterChain configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf((csrf) -> csrf.disable())
                .exceptionHandling(exception -> exception.authenticationEntryPoint(new AuthEntryPointJwt()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests((authorize) ->
                        authorize
                                .requestMatchers("/").permitAll()
                                .requestMatchers("/api/auth/**").permitAll()
                                .anyRequest().authenticated());


        // adding the custom filter before UsernamePasswordAuthenticationFilter in the filter chain
        httpSecurity.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }


}