package com.vincent.springrest.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;


@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	@Autowired
	@Qualifier("customUserDetailsService")
	UserDetailsService userDetailsService;
	
    @Autowired
    private RestUnauthorizedEntryPoint restAuthenticationEntryPoint;

    @Autowired
    private AccessDeniedHandler restAccessDeniedHandler;

    @Autowired
    private AuthenticationSuccessHandler restAuthenticationSuccessHandler;

    @Autowired
    private AuthenticationFailureHandler restAuthenticationFailureHandler;


	private static String REALM="MY_TEST_REALM";
	static final Logger logger = LoggerFactory.getLogger(SecurityConfiguration.class);
	public static final String REMEMBER_ME_KEY = "rememberme_key";

	@Autowired
	public void configureGlobalSecurity(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService);
		auth.authenticationProvider(authenticationProvider());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		//		http          
		//		.sessionManagement()
		//		.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		//		.and()
		//		.authorizeRequests()
		//		.antMatchers(HttpMethod.GET, "/api/**").authenticated()
		//		.antMatchers(HttpMethod.POST, "/api/**").authenticated()
		//		.antMatchers(HttpMethod.PUT, "/api/**").authenticated()
		//		.antMatchers(HttpMethod.DELETE, "/api/**").authenticated()
		//		.and()
		//		.httpBasic().realmName(REALM).authenticationEntryPoint(getBasicAuthEntryPoint())
		//		.and()
		//		.csrf().disable();

		http
		.headers().disable()
		.httpBasic().realmName(REALM).authenticationEntryPoint(getBasicAuthEntryPoint())
		.and()
		.csrf().disable()
		.authorizeRequests()
		.antMatchers("/failure").permitAll()
		.antMatchers("/api/**").hasRole("ADMIN")
		.anyRequest().authenticated()
		.and()
		.exceptionHandling()
		.authenticationEntryPoint(restAuthenticationEntryPoint)
		.accessDeniedHandler(restAccessDeniedHandler)
		.and()
		.formLogin()
		.loginProcessingUrl("/authenticate")
		.successHandler(restAuthenticationSuccessHandler)
		.failureHandler(restAuthenticationFailureHandler)
		.usernameParameter("username")
		.passwordParameter("password")
		.permitAll()
		.and()
		.logout()
		.logoutUrl("/logout")
		.logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler())
		.deleteCookies("JSESSIONID")
		.permitAll().and();
	}

	@Bean
	public CustomBasicAuthenticationEntryPoint getBasicAuthEntryPoint(){
		return new CustomBasicAuthenticationEntryPoint();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(userDetailsService);
		authenticationProvider.setPasswordEncoder(passwordEncoder());
		return authenticationProvider;
	}


	@Bean
	public AuthenticationTrustResolver getAuthenticationTrustResolver() {
		return new AuthenticationTrustResolverImpl();
	}
}
