package be.kdg.youthcouncil.config.security;

import be.kdg.youthcouncil.config.security.Oauth.CustomOAuth2UserService;
import be.kdg.youthcouncil.config.security.Oauth.OAuthLoginSuccessHandler;
import be.kdg.youthcouncil.config.security.abac.ApiRequestVoter;
import be.kdg.youthcouncil.config.security.abac.RequestVoter;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.vote.AuthenticatedVoter;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.access.vote.UnanimousBased;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionVoter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import java.util.ArrayList;
import java.util.List;


@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class WebSecurityConfig {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final CustomOAuth2UserService oauthUserService;
	private final OAuthLoginSuccessHandler oauthLoginSuccessHandler;
	private final CustomLoginSuccessHandler loginSuccessHandler;
	private final ApiRequestVoter apiRequestVoter;
	private final RequestVoter requestVoter;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		logger.debug("filterChain setup");
		//http.authorizeRequests().antMatchers("/**").permitAll().anyRequest().authenticated().and().csrf().disable();

		http
				.httpBasic()
				.and()
				.csrf()
				.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
				.and()
				.authorizeRequests(auths -> auths
						.antMatchers(HttpMethod.GET, "/", "/logout", "/login", "/register", "/oauth/**", "/youthcouncils", "/youthcouncils/**")
						.permitAll()
						.antMatchers(HttpMethod.GET, "/js/**", "/css/**", "/webjars/**", "/favicon.ico", "/api/municipalities/")
						.permitAll()
						.regexMatchers(HttpMethod.POST, "/api/youthcouncils/\\d/\\d", "/api/ideas")
						.permitAll()
						.antMatchers(HttpMethod.POST, "/api/youthcouncils/**")
						.hasRole("COUNCIL_ADMIN")
						.regexMatchers(HttpMethod.PATCH, "/api/notifications/\\d")
						.hasRole("USER")
						.regexMatchers(HttpMethod.PUT, "/api/actionpoints/\\d/\\d")
						.hasRole("COUNCIL_ADMIN")
						.regexMatchers(HttpMethod.DELETE, "/api/actionpoints/subscribe/\\d/\\d")
						.hasRole("USER")
						.regexMatchers(HttpMethod.POST, "/api/actionpoints/subscribe/\\d/\\d")
						.hasRole("USER")
						.regexMatchers(HttpMethod.PATCH, "/api/actionpoints/\\d/\\d")
						.hasRole("COUNCIL_ADMIN")
						.regexMatchers(HttpMethod.PATCH, "/api/users/\\d/role")
						.hasRole("COUNCIL_ADMIN")
						.regexMatchers(HttpMethod.PATCH, "/api/users/\\d/blocked-status")
						.hasRole("COUNCIL_ADMIN")
						.anyRequest()
						.authenticated()
						.accessDecisionManager(accessDecisionManager())
				)
				.formLogin()
				.loginPage("/login")
				.permitAll()
				.and()
				.logout()
				.permitAll()
				.and()
				.oauth2Login()
				.loginPage("/login")
				.successHandler(loginSuccessHandler)
				.userInfoEndpoint()
				.userService(oauthUserService)
				.and()
				.successHandler(oauthLoginSuccessHandler);

		return http.build();
	}

	@Bean
	public AccessDecisionManager accessDecisionManager() {
		List<AccessDecisionVoter<?>> decisionVoters = new ArrayList<>();
		decisionVoters.add(new RoleVoter());
		decisionVoters.add(new AuthenticatedVoter());
		decisionVoters.add(new WebExpressionVoter());
		decisionVoters.add(apiRequestVoter);
		decisionVoters.add(requestVoter);
		return new UnanimousBased(decisionVoters);
	}


}
