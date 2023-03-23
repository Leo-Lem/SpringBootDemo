package leolem.demo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import leolem.demo.security.jwt.AuthTokenFilter;

@Configuration
public class SecurityConfiguration {

  @Autowired
  AppUserDetailsService userDetailsService;

  @Autowired
  UserIDAuthorizationManager userIDAuthorizationManager;

  @Bean
  public AuthTokenFilter authenticationJwtTokenFilter() {
    return new AuthTokenFilter();
  }

  @Bean
  public DaoAuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

    authProvider.setUserDetailsService(userDetailsService);
    authProvider.setPasswordEncoder(passwordEncoder());

    return authProvider;
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
    return authConfig.getAuthenticationManager();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity security) throws Exception {
    security.cors().and().csrf().disable()
        .authorizeHttpRequests(authorize -> authorize
            .requestMatchers(HttpMethod.GET, "/books", "/books/**").permitAll()
            .requestMatchers(HttpMethod.POST, "/users", "/users/signin").permitAll())
        .authorizeHttpRequests(authorize -> authorize
            .requestMatchers(HttpMethod.GET, "/users/**", "/books", "/books/**").hasRole("USER")
            .requestMatchers(HttpMethod.POST, "/books").hasRole("USER"))
        .authorizeHttpRequests(authorize -> authorize
            .requestMatchers("/users/{userID}", "/borrow/*/{userID}").access(userIDAuthorizationManager)
            .requestMatchers("/**").hasRole("ADMIN"))
        .authorizeHttpRequests().anyRequest().denyAll().and()
        .httpBasic();

    security.authenticationProvider(authenticationProvider());

    security.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

    return security.build();
  }

}