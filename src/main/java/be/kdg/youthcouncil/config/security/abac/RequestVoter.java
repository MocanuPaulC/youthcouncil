package be.kdg.youthcouncil.config.security.abac;

import be.kdg.youthcouncil.config.security.CustomUserDetails;
import be.kdg.youthcouncil.domain.youthcouncil.YouthCouncil;
import be.kdg.youthcouncil.domain.youthcouncil.subscriptions.YouthCouncilSubscription;
import be.kdg.youthcouncil.exceptions.YouthCouncilSubscriptionNotFoundException;
import be.kdg.youthcouncil.service.users.UserService;
import be.kdg.youthcouncil.service.youthcouncil.YouthCouncilService;
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
public class RequestVoter implements AccessDecisionVoter<FilterInvocation> {

	private final Logger logger = LoggerFactory.getLogger(ApiRequestVoter.class);
	private final UserService userService;
	private final YouthCouncilService youthCouncilService;
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

		if (uri.contains("api") || collection.toString().equals("[authenticated]")) {
			return ACCESS_ABSTAIN;
		}

		logger.debug(collection.toString());
		logger.debug(filterInvocation.toString());

		if (collection.stream()
		              .anyMatch(a -> "permitAll".equals(a.toString()))) {
			logger.debug("quick exit");
			if (uri.contains("youthcouncils/") && authentication.getPrincipal() instanceof CustomUserDetails user) {
				String[] splitUri = uri.split("/");
				try {
					YouthCouncilSubscription subscription = youthCouncilSubscriptionService.findAllByUserIdAndYouthCouncilMunicipality(user.getUserId(), splitUri[2]);
					if (subscription.isBlocked()) {
						return ACCESS_DENIED;
					}
				} catch (YouthCouncilSubscriptionNotFoundException e) {
					return ACCESS_GRANTED;
				}

			}
			return ACCESS_GRANTED;
		}

		CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();

		String collectionRoleRequest = collection.stream().findFirst()
		                                         .map(ConfigAttribute::toString)
		                                         .map(s -> s.substring(s.indexOf("_") + 1, s.indexOf("')")))
		                                         .orElse(null);

		if (filterInvocation.getHttpRequest().getHeader("youthCouncilID") == null) {
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

		if ((user.isGa() && "GENERAL_ADMIN".equals(collectionRoleRequest))) {
			return ACCESS_GRANTED;
		} else if (!user.isGa() && "GENERAL_ADMIN".equals(collectionRoleRequest)) {
			return ACCESS_DENIED;
		}

		if (uri.startsWith("/youthcouncils/")) {
			String[] splitUri = uri.split("/");
			YouthCouncil yc = youthCouncilService.findByMunicipality(splitUri[2]);

			String currentSubscription;
			try {
				currentSubscription = userService.findSubscriptionRoleOfUserToYouthCouncil(user.getUserId(), yc.getYouthCouncilId())
				                                 .toString();
			} catch (YouthCouncilSubscriptionNotFoundException e) {
				return ACCESS_DENIED;
			}
			if (currentSubscription.equals(collectionRoleRequest)) {
				return ACCESS_GRANTED;
			} else {
				return ACCESS_DENIED;
			}

		}

		return ACCESS_ABSTAIN;
	}
}
