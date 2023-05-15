package be.kdg.youthcouncil.config.security.abac;

import be.kdg.youthcouncil.service.users.UserService;
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

		if (!collection.toString().contains("ROLE_OWNER")) {
			return ACCESS_ABSTAIN;
		}


		try {
			String collectionRoleRequest = collection.stream().findFirst()
			                                         .map(ConfigAttribute::toString)
			                                         .map(s -> s.substring(s.indexOf("_") + 1, s.indexOf("')")))
			                                         .orElse(null);
		} catch (StringIndexOutOfBoundsException e) {
		}

		return 0;
	}
}
