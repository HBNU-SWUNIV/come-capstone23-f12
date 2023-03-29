package io.f12.notionlinkedblog.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.token.SecureRandomFactoryBean;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
public class SecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.authorizeRequests().anyRequest().permitAll();

		http
			.headers().frameOptions().disable()
			.and()
			.csrf().disable();

		return http.build();
	}

	@Bean
	public SecureRandomFactoryBean secureRandomFactoryBean() {
		return new SecureRandomFactoryBean();
	}
}
