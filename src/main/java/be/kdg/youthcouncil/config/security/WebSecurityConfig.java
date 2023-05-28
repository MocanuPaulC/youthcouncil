package be.kdg.youthcouncil.config.security;

import be.kdg.youthcouncil.config.security.Oauth.CustomOAuth2UserService;
import be.kdg.youthcouncil.config.security.Oauth.OAuthLoginSuccessHandler;
import be.kdg.youthcouncil.config.security.abac.ApiRequestVoter;
import be.kdg.youthcouncil.config.security.abac.RequestVoter;
import be.kdg.youthcouncil.config.security.abac.UserRequestVoter;
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
	private final UserRequestVoter userRequestVoter;

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
						.antMatchers(HttpMethod.GET, "/", "/logout", "/login", "/register", "/oauth/**", "/js/**", "/css/**", "/webjars/**", "/favicon.ico", "/error")
						.permitAll()
						.antMatchers(HttpMethod.POST, "/register")
						.permitAll()
						//----------------------/
						//---      OWNER     ---/
						//----------------------/
						.antMatchers(
								HttpMethod.GET,
								"/profile"
						).hasRole("OWNER")
						.antMatchers(
								HttpMethod.PATCH,
								"/api/users/*/password", "/api/users/*/username"
						).hasRole("OWNER")
						.regexMatchers(HttpMethod.POST, "/api/users/\\d/password"
						).hasRole("OWNER")
						//----------------------/
						//---  GENERAL ADMIN ---/
						//----------------------/
						.antMatchers(
								HttpMethod.GET,
								"/youthcouncils/add/*",
								"/user-management",
								"/statistics", "/users",
								"/youthcouncils/add",
								"/youthcouncils/*/create-council-admin",

								"/informativepages/create", "/informativepages/*/edit",
								"/api/informativepages/informativepageblocks/*"
						).hasRole("GENERAL_ADMIN")
						.antMatchers(
								HttpMethod.POST,
								"/youthcouncils/add",
								"/youthcouncils/*/create-council-admin",
								"/api/informativepages/*"
						).hasRole("GENERAL_ADMIN")
						.antMatchers(
								HttpMethod.PUT,
								"/api/informativepages/*"
						).hasRole("GENERAL_ADMIN")
						//----------------------/
						//---  COUNCIL_ADMIN ---/
						//----------------------/
						.antMatchers(
								HttpMethod.GET,
								"/youthcouncils/*/user-management",
								"/youthcouncils/*/edit", "/youthcouncils/*/statistics",
								"/youthcouncils/*/callforideas/*/createactionpoint",
								"/youthcouncils/*/announcements/add",
								"/youthcouncils/*/informativepages/create", "/youthcouncils/*/informativepages/*/edit",
								"/api/informativepages/informativepageblocks/*/*",
								"/youthcouncils/*/actionpoints/create",
								"/youthcouncils/*/actionpoints/*/edit",
								"/api/actionpoints/actionpointblocks/*/*",
								"/youhtcouncils/*/callforideas/*/createactionpoint",
								"/api/ideas/*/subthemes"
						).hasRole("COUNCIL_ADMIN")
						.antMatchers(
								//								http://localhost:8081/api/action-points/blocktypes
								HttpMethod.POST,
								"/youthcouncils/*/announcements/add",
								"/api/informativepages/*/*",
								"/api/actionpoints/*/*",
								"/api/actionpoints/create/*/*/*",
								"/api/youthcouncils/*/callforideas",
								"/api/ideas", "youthcouncils/*/action-points/create",
								"/youthcouncils/*/action-points/edit",
								"/api/actionpoints/createwithideas/*/*/*",
								"/api/ideas/*/uploadFile"
						).hasRole("COUNCIL_ADMIN")
						.antMatchers(
								HttpMethod.PATCH,
								"/api/users/*/role",
								"/api/users/*/blocked-status",
								"/api/actionpoints/*/linkideas/*/*",
								"/api/actionpoints/*/*",
								"/api/call-for-ideas/*/set-display",
								"/api/announcements/*/set-display"
						).hasRole("COUNCIL_ADMIN")
						.antMatchers(
								HttpMethod.PUT,
								"/api/informativepages/*/*",
								"/api/actionpoints/create/*/*/*",
								"/api/actionpoints/*/*/*/PUT",
								"/api/actionpoints/*/*"
						).hasRole("COUNCIL_ADMIN")
						//----------------------/
						//---       USER     ---/
						//----------------------/
						.antMatchers(
								HttpMethod.GET,
								"/api/idea-reaction/*/*"
						).hasRole("USER")
						.antMatchers(
								HttpMethod.POST,
								"/api/media/upload",
								"/api/actionpoints/subscribe/*/*",
								"/api/idea-reaction/react",
								"/api/ideas",
								"/api/actionpoints/subscribe/*/*"
						).hasRole("USER")
						.antMatchers(
								HttpMethod.PATCH,
								"/api/notifications/*"
						)
						.hasRole("USER")
						.antMatchers(
								HttpMethod.DELETE,
								"/api/users/*",
								"/api/actionpoints/subscribe/*/*"
						)
						.hasRole("USER")
						//----------------------/
						//---        ALL     ---/
						//----------------------/
						.antMatchers(HttpMethod.GET,
								"/youthcouncils", "/youthcouncils/*", "/api/municipalities/",
								"/youthcouncils/*/informativepages", "/youthcouncils/*/informativepages/*", "/informativepages/*",
								"/youthcouncils/*/actionpoints", "/youthcouncils/*/actionpoints/*",
								"/youthcouncils/*/callforideas", "/youthcouncils/*/callforideas/*",
								"/youthcouncils/*/announcements", "/youthcouncils/*/announcements/*",
								"/api/municipalities", "/api/informativepages/blocktypes",
								"/api/media/imagename", "/ws/**",
								"/api/qrcode/actionpoint/*/*"
						)
						.permitAll()
						// and this one
						.antMatchers("/api/action-point-reaction/react", "/api/action-point-reaction/*/*")
						.authenticated()
						.antMatchers(HttpMethod.POST, "/api/youthcouncils/*/*")
						.permitAll()
						.antMatchers(HttpMethod.DELETE, "/api/youthcouncils/*/*")
						.permitAll()
						.anyRequest()
						.denyAll()
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
		decisionVoters.add(userRequestVoter);
		return new UnanimousBased(decisionVoters);
	}
}
