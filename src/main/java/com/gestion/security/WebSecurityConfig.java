package com.gestion.security;

import com.gestion.model.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;


/**
 * Custom web adapter for authentication and authorization
 */

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {


    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // Enable CORS and disable CSRF
        http.cors().and().csrf().disable()
                .authorizeHttpRequests((requests) -> requests
//                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
//                .authorizeHttpRequests()
                        .requestMatchers("/").permitAll()
//                        .requestMatchers("/registration").permitAll()
                        .requestMatchers("/login").permitAll()
                        .requestMatchers("/access-denied").permitAll()
                        .requestMatchers("/show-update-password-step1-form").permitAll()
                        .requestMatchers("/update-password-step1").permitAll()
                        .requestMatchers("/show-update-password-step2-form/**").permitAll()
                        .requestMatchers("/update-password-step2/**").permitAll()
//                        .requestMatchers("//**").permitAll()

//                        .requestMatchers("/account/register/client").permitAll()
//                        .requestMatchers("/account/login").permitAll()
//                        .requestMatchers("/account/register/send-code").permitAll()
//                        .requestMatchers("/account/register/activation").permitAll()
//                        .requestMatchers("/account/reset-password/send-code").permitAll()
//                        .requestMatchers("/account/reset-password/validation").permitAll()
                        .requestMatchers("/user/**").hasAnyAuthority(User.ROLE.EMPLOYEE.name(), User.ROLE.ADMIN.name())
        //                .antMatchers("/institution/**").permitAll()
                        .requestMatchers("/admin/**").hasAnyAuthority(User.ROLE.ADMIN.name())
                        .anyRequest().authenticated())
                        .formLogin().loginPage("/login").loginProcessingUrl("/login")
                        .defaultSuccessUrl("/user/product-list")
                        .failureUrl("/login?error=true")
        //                .failureHandler(getAuthenticationFailureHandler())
                        .permitAll()
                        .and().logout().logoutUrl("/logout").logoutSuccessUrl("/login?logout=true")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                        .and().exceptionHandling().accessDeniedPage("/access-denied");
//        http.addFilterAfter(
//                new CustomFilter(), BasicAuthenticationFilter.class);

        return http.build();
    }


}
