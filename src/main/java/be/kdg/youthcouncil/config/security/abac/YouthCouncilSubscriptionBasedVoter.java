package be.kdg.youthcouncil.config.security.abac;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.FilterInvocation;

import java.util.Collection;

public class YouthCouncilSubscriptionBasedVoter implements AccessDecisionVoter {

	private final Logger logger = LoggerFactory.getLogger(YouthCouncilSubscriptionBasedVoter.class);

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
		//		filterInvocation.getHttpRequest().getCookies();
		if (uri.contains("api") || authentication.getPrincipal() instanceof String || !uri.contains("/youthcouncils/"))
			return ACCESS_ABSTAIN;
		if (collection.stream()
		              .anyMatch(a -> a.toString()
		                              .equals("permitAll")) || (authentication.getAuthorities()
		                                                                      .toString()
		                                                                      .contains("GENERAL_ADMIN")))
			return ACCESS_GRANTED;


		String municipalityAccessed = uri.split("/")[2];


		//		logger.debug(authentication.getPrincipal().toString());

		//		List<YouthCouncilSubscription> subscription = user.getYouthCouncilSubscriptions();
		if (authentication.getAuthorities()
		                  .stream()
		                  .map(GrantedAuthority::getAuthority)
		                  .anyMatch(authority -> authority.contains("COUNCIL_ADMIN@" + municipalityAccessed) ||
				                  authority.contains("USER@" + municipalityAccessed)
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

