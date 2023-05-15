package be.kdg.youthcouncil.config.email;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConfigurationProperties (prefix = "spring.mail")
@Getter
public class EmailProperties {

	private final String username;
	private final String password;

	@ConstructorBinding
	public EmailProperties(String username, String password) {
		this.username = username;
		this.password = password;
	}


}
