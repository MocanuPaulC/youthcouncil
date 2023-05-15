package be.kdg.youthcouncil.config.security.abac;

import be.kdg.youthcouncil.config.security.CustomUserDetails;
import be.kdg.youthcouncil.domain.youthcouncil.subscriptions.SubscriptionRole;
import be.kdg.youthcouncil.domain.youthcouncil.subscriptions.YouthCouncilSubscription;
import be.kdg.youthcouncil.exceptions.YouthCouncilSubscriptionNotFoundException;
import be.kdg.youthcouncil.service.users.UserService;
import be.kdg.youthcouncil.service.youthcouncil.subscriptions.YouthCouncilSubscriptionService;
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
public class ApiRequestVoter implements AccessDecisionVoter<FilterInvocation> {

	private final Logger logger = LoggerFactory.getLogger(ApiRequestVoter.class);

	private final UserService userService;

	private final YouthCouncilSubscriptionService youthCouncilSubscriptionService;

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

		if (!uri.contains("api") || collection.toString().equals("[authenticated]")) {
			return ACCESS_ABSTAIN;
		}
		if (youthCouncilIdHeader == null && !uri.contains("notifications")) {
			throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "youthCouncilID header is missing");
		}


		if (collection.stream()
		              .anyMatch(a -> "permitAll".equals(a.toString()))) {
			return ACCESS_GRANTED;
		}

		CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();

		String collectionRoleRequest = collection.stream().findFirst()
		                                         .map(ConfigAttribute::toString)
		                                         .map(s -> s.substring(s.indexOf("_") + 1, s.indexOf("')")))
		                                         .orElse(null);

		if ((user.isGa() && "GENERAL_ADMIN".equals(collectionRoleRequest))) return ACCESS_GRANTED;

		logger.debug("Request for {} permission received", collectionRoleRequest);
		if (youthCouncilIdHeader != null) {
			int youthCouncilId = Integer.parseInt(youthCouncilIdHeader);

			try {
				YouthCouncilSubscription subscription = youthCouncilSubscriptionService.findAllByUserIdAndYouthCouncilId(user.getUserId(), youthCouncilId);
				if (subscription.isBlocked() || subscription.isDeleted()) return ACCESS_DENIED;
				if (subscription.getRole().equals(SubscriptionRole.valueOf(collectionRoleRequest))) {
					return ACCESS_GRANTED;
				} else {
					return ACCESS_DENIED;
				}
			} catch (YouthCouncilSubscriptionNotFoundException e) {
				logger.debug(e.getMessage());
				return ACCESS_DENIED;
			}
		} else {
			logger.error("YouthCouncilID header is missing, make sure you put it in the header of your request");
			logger.error("That is if you are trying to access anything related to a youth council");
			if (authentication.getAuthorities()
			                  .stream()
			                  .anyMatch(a -> a.getAuthority().equals("ROLE_" + collectionRoleRequest))) {
				return ACCESS_GRANTED;
			} else {
				return ACCESS_DENIED;
			}
		}
	}
}
