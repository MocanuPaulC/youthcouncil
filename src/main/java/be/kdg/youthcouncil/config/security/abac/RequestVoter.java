package be.kdg.youthcouncil.config.security.abac;

import be.kdg.youthcouncil.config.security.CustomUserDetails;
import be.kdg.youthcouncil.domain.youthcouncil.subscriptions.SubscriptionRole;
import be.kdg.youthcouncil.exceptions.YouthCouncilSubscriptionNotFoundException;
import be.kdg.youthcouncil.service.users.UserService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.FilterInvocation;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Collection;

@Component
@AllArgsConstructor
public class RequestVoter implements AccessDecisionVoter<FilterInvocation> {

	private final Logger logger = LoggerFactory.getLogger(RequestVoter.class);

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
		String youthCouncilIdHeader = filterInvocation.getHttpRequest().getHeader("youthCouncilID");

		if (!uri.contains("api")) {
			return ACCESS_ABSTAIN;
		}
		if (youthCouncilIdHeader == null) {
			throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "youthCouncilID header is missing");
		}
		CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();

		if (collection.stream()
		              .anyMatch(a -> "permitAll".equals(a.toString()))) {
			return ACCESS_GRANTED;
		}


		String collectionRoleRequest = collection.stream().findFirst()
		                                         .map(ConfigAttribute::toString)
		                                         .map(s -> s.substring(s.indexOf("_") + 1, s.indexOf("')")))
		                                         .orElse(null);

		if ((user.isGa() && "GENERAL_ADMIN".equals(collectionRoleRequest))) return ACCESS_GRANTED;

		logger.debug("Request for {} permission received", collectionRoleRequest);

		int youthCouncilId = Integer.parseInt(youthCouncilIdHeader);

		try {
			if (userService.findSubscriptionRoleOfUserToYouthCouncil(user.getUserId(), youthCouncilId)
			               .equals(SubscriptionRole.valueOf(collectionRoleRequest))) {
				return ACCESS_GRANTED;
			} else {
				return ACCESS_DENIED;
			}
		} catch (YouthCouncilSubscriptionNotFoundException e) {
			logger.debug(e.getMessage());
			return ACCESS_DENIED;
		}
	}
}
