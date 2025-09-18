package com.yoim.www.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;
import org.springframework.security.web.header.writers.StaticHeadersWriter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired AuthProvider authProvider;
    @Autowired AuthFailureHandler authFailureHandler;
    @Autowired AuthSuccessHandler authSuccessHandler;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authProvider);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /** CORS 허용 오리진 */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration c = new CorsConfiguration();
        c.setAllowCredentials(true);
        c.setAllowedOrigins(Arrays.asList(
                "http://localhost:3000",
                "http://127.0.0.1:3000",
                "http://localhost:8200",
                "https://yoim.kr",
                "https://app.yoim.kr"
        ));
        c.setAllowedMethods(Arrays.asList("GET","POST","PUT","DELETE","OPTIONS","HEAD"));
        c.setAllowedHeaders(Arrays.asList("Authorization","Content-Type","X-Requested-With","Accept","X-CSRF-TOKEN"));
        c.setExposedHeaders(Arrays.asList("Authorization"));
        c.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource src = new UrlBasedCorsConfigurationSource();
        src.registerCorsConfiguration("/**", c);
        return src;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	http
        .cors().and()
        .csrf().disable()

        .authorizeRequests()
            .antMatchers(org.springframework.http.HttpMethod.OPTIONS, "/**").permitAll()
            .antMatchers("/", "/doLogin", "/logout",
                         "/resource/**", "/main/**",
                         "/error", "/favicon.ico").permitAll()
            .anyRequest().permitAll()
        .and()

        .formLogin()
            .loginPage("/")
            .loginProcessingUrl("/doLogin")
            .usernameParameter("param1")
            .passwordParameter("param2")
            .failureHandler(authFailureHandler)
            .successHandler(authSuccessHandler)
            .permitAll()
        .and()

        .logout()
            .logoutUrl("/logout")
            .deleteCookies("JSESSIONID")
            .invalidateHttpSession(true)
            .logoutSuccessUrl("/");
    }
}
