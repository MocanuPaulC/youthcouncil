package be.kdg.youthcouncil.config.security.abac;

import be.kdg.youthcouncil.config.security.CustomUserDetails;
import be.kdg.youthcouncil.service.users.UserService;
import be.kdg.youthcouncil.service.youthcouncil.subscriptions.YouthCouncilSubscriptionService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.FilterInvocation;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
@AllArgsConstructor
public class UserRequestVoter implements AccessDecisionVoter<FilterInvocation> {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final UserService userService;

	@Override
	public boolean supports(ConfigAttribute attribute) {
		return true;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return true;
	}

	@Override
	public int vote(Authentication authentication, FilterInvocation filterInvocation, Collection<ConfigAttribute> collection) {
		String uri = filterInvocation.getRequest().getRequestURI();

		logger.debug("we are getting in here");
		logger.debug(collection.toString());
		if (!collection.toString().contains("ROLE_OWNER")) {
			return ACCESS_ABSTAIN;
		}


		try {
			String collectionRoleRequest = collection.stream().findFirst()
			                                         .map(ConfigAttribute::toString)
			                                         .map(s -> s.substring(s.indexOf("_") + 1, s.indexOf("')")))
			                                         .orElse(null);
			if (collectionRoleRequest.contains("OWNER")) {
				CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
				Long id = Long.parseLong(uri.split("/")[3]);
				if (user.getUserId() != id) {
					return ACCESS_DENIED;
				} else {
					return ACCESS_GRANTED;
				}
			} else {
				return ACCESS_DENIED;
			}
		} catch (StringIndexOutOfBoundsException e) {
			logger.debug("helllpppp");
		}

		return 0;
	}
}
