package be.kdg.youthcouncil.config.security.abac;

import be.kdg.youthcouncil.domain.users.PlatformUser;
import be.kdg.youthcouncil.service.youthcouncil.YouthCouncilService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.FilterInvocation;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class ApiYouthCouncilSubscriptionBasedVoter implements AccessDecisionVoter {


	private final Logger logger = LoggerFactory.getLogger(ApiYouthCouncilSubscriptionBasedVoter.class);
	private YouthCouncilService youthCouncilService;


	@Autowired
	public ApiYouthCouncilSubscriptionBasedVoter(YouthCouncilService youthCouncilService) {
		this.youthCouncilService = youthCouncilService;
	}

	@Override
	public boolean supports(ConfigAttribute attribute) {
		return true;
	}

	@Override
	public boolean supports(Class clazz) {
		return true;
	}

	@Override
	public int vote(Authentication authentication, Object object, Collection collection) {
		FilterInvocation filterInvocation = (FilterInvocation) object;
		String uri = filterInvocation.getRequest().getRequestURI();
		String requestHeader = filterInvocation.getHttpRequest().getHeader("Access-Control-Request-Header");

		//		filterInvocation.getHttpRequest().getCookies();
		System.out.println("requestHeader = " + requestHeader);
		if (!uri.contains("api") ||
				authentication.getPrincipal() instanceof String ||
				!uri.contains("/youthcouncils/") ||
				!requestHeader.equals("COUNCIL_ADMIN"))
			return ACCESS_ABSTAIN;
		if (collection.stream()
		              .anyMatch(a -> a.toString()
		                              .equals("permitAll")) || ((PlatformUser) authentication.getPrincipal()).isGA())
			return ACCESS_GRANTED;


		String youthCouncilIdAccessed = uri.split("/")[3];

		if (authentication.getAuthorities()
		                  .stream()
		                  .map(GrantedAuthority::getAuthority)
		                  .anyMatch(authority -> authority.contains("COUNCIL_ADMIN@" + youthCouncilIdAccessed) ||
				                  authority.contains("USER@" + youthCouncilIdAccessed)
		                  )
			//			                       &&
			//			                       subscription.stream()
			//			                                   .anyMatch(s -> s.getYouthCouncil()
			//			                                                   .getYouthCouncilId() == youthCouncil.getYouthCouncilId())
		) {
			return ACCESS_GRANTED;
		} else {
			return ACCESS_DENIED;
		}

	}
}
