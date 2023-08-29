package com.ecom.config;

import com.ecom.Security.JwtAuthenticationEntryPoint;
import com.ecom.Security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.bind.annotation.CrossOrigin;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@CrossOrigin("*")
public class SecurityConfig {


    @Autowired
    private CustomUserDetailsService customUserDetailsService;

//    public static String[] PUBLIC_URL= {"/auth/login","/cart/**"};
    public static String[] PUBLIC_URL= {"/auth/login"};

    @Autowired
    private JwtAuthenticationFilter filter;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtAuthenticationEntryPoint entryPoint;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((authorize) -> authorize.requestMatchers(PUBLIC_URL)
                .permitAll().anyRequest().authenticated())
                .exceptionHandling(exception -> exception.authenticationEntryPoint(entryPoint))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        //removed http basic due to involvement of jwtAuthentication
        //we have to use lambda function for this

        http.authenticationProvider(authenticationProvider());
        http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
//         http.httpBasic(withDefaults());
//        AuthenticationManagerBuilder authenticationManagerBuilder =
//         http.getSharedObject(AuthenticationManagerBuilder.class);
//        authenticationManagerBuilder.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder());
        return http.build();
    }

    @Bean
    public JwtAuthenticationFilter authenticationFilter() {
        return new JwtAuthenticationFilter();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(customUserDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

//    @Bean
//    public FilterRegistrationBean corsFilter() {
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.setAllowCredentials(true);
//        configuration.addAllowedOriginPattern("*");
//        configuration.addAllowedHeader("Authorization");
//        configuration.addAllowedHeader("Context-Type");
//        configuration.addAllowedHeader("Accept");
//        configuration.addAllowedMethod("POST");
//        configuration.addAllowedMethod("GET");
//        configuration.addAllowedMethod("PUT");
//        configuration.addAllowedMethod("DELETE");
//        configuration.addAllowedMethod("OPTIONS");
//        configuration.setMaxAge(3600L);
//        source.registerCorsConfiguration("/**",configuration);
//        FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
//        bean.setOrder(-110);
//        return bean;
//    }
}
