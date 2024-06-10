package com.foxminded.university_schedule.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.foxminded.university_schedule.model.entity.Permission;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

	private UserDetailsService userDetailsService;
	
	public SecurityConfig(@Qualifier("userDetailsServiceImpl") UserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
		.csrf().disable()
        .authorizeHttpRequests(authorize -> authorize
        		.requestMatchers("/menu", "/auth/registration").permitAll()
        		.requestMatchers("/group/**", "/course/**", "/student/**", "/teacher/**", "/schedule/**").authenticated()
        		.requestMatchers("/**").hasAuthority(Permission.EDIT_USERS.getName()))
        .formLogin(form -> form
        		.loginPage("/auth/login")
        		.failureUrl("/auth/login-error")
        		.defaultSuccessUrl("/menu", true).permitAll())
        .logout(logout -> logout
        		.logoutUrl("/auth/logout").permitAll()
        		.invalidateHttpSession(true)
        		.clearAuthentication(true)
        		.deleteCookies("JSESSIONID")
        		.logoutSuccessUrl("/auth/login"));
		return http.build();
	}
	
	@Bean
	public DaoAuthenticationProvider daoAuthenticationProvider() {
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
		daoAuthenticationProvider.setUserDetailsService(userDetailsService);
		return daoAuthenticationProvider;
	}
}
