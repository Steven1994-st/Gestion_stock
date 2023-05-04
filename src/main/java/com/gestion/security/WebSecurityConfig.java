package com.gestion.security;

import com.gestion.model.User;
import com.gestion.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


/**
 * Custom web adapter for authentication and authorization
 */

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {

//    @Autowired
//    BCryptPasswordEncoder bCryptPasswordEncoder;
//
//    @Autowired
//    private AuthEntryPointJwt unauthorizedHandler;

//    @Autowired
//    private AccessDeniedHandler accessDeniedHandler;

    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailsService();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    @Bean
//    public JwtTokenFilter authenticationJwtTokenFilter() {
//        return new JwtTokenFilter();
//    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder bCryptPasswordEncoder, UserDetailsService userDetailsService)
            throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailsService)
                .passwordEncoder(bCryptPasswordEncoder)
                .and()
                .build();
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
                        .defaultSuccessUrl("/admin/user-list")
                        .permitAll()
                        .and().logout().permitAll()
                        .and().exceptionHandling().accessDeniedPage("/access-denied");
//        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


}
