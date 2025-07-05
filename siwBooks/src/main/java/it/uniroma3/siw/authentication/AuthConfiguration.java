package it.uniroma3.siw.authentication;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import it.uniroma3.siw.model.Credenziali;
import it.uniroma3.siw.model.Utente;

@Configuration
@EnableWebSecurity
public class AuthConfiguration {

	@Autowired
	DataSource datasource;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.authorizeHttpRequests(requests -> {
				requests
					.requestMatchers(HttpMethod.GET, "/").permitAll()
					.requestMatchers(HttpMethod.GET, "/login").permitAll()
					.requestMatchers(HttpMethod.GET, "/ristoranti").permitAll()
					.requestMatchers(HttpMethod.GET, "/ristorante/**").permitAll()
					.requestMatchers(HttpMethod.GET, "/register").permitAll()
					.requestMatchers(HttpMethod.GET, "/css/**").permitAll()
					.requestMatchers(HttpMethod.GET, "/static/**").permitAll()
					.requestMatchers(HttpMethod.GET, "/images/**").permitAll()
					.requestMatchers(HttpMethod.GET, "/logo.png").permitAll()
					.requestMatchers(HttpMethod.GET, "/torsoliPattern.png").permitAll()
					.requestMatchers(HttpMethod.GET, "/favicon.ico").permitAll()
					.requestMatchers(HttpMethod.POST, "/login", "/register").permitAll()
					.requestMatchers(HttpMethod.GET, "/ritiro/**").authenticated()
					.requestMatchers(HttpMethod.POST, "/ritiro/**").authenticated()
					.requestMatchers(HttpMethod.GET, "/admin/**").hasAnyAuthority(Utente.ADMIN_ROLE)
					.requestMatchers(HttpMethod.POST, "/admin/**").hasAnyAuthority(Utente.ADMIN_ROLE)
					.anyRequest().authenticated();
			})
			.exceptionHandling(handling -> handling.accessDeniedPage("/"))
			.formLogin(login -> login
				.loginPage("/login")
				.defaultSuccessUrl("/", true)
				.permitAll()
			)
			.logout(logout -> logout
				.logoutUrl("/logout")
				.logoutSuccessUrl("/")
				.invalidateHttpSession(true)
				.deleteCookies("JSESSIONID")
				.clearAuthentication(true).permitAll()
			);
		return http.build();
	}

    @Bean
    UserDetailsService userDetailsService() {
		JdbcUserDetailsManager manager = new JdbcUserDetailsManager(datasource);
		manager.setUsersByUsernameQuery("SELECT username, password, 1 as enabled FROM credenziali WHERE username=?");
		manager.setAuthoritiesByUsernameQuery("SELECT username, ruolo FROM credenziali WHERE username=?");
		return manager;
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
