package be.kdg.youthcouncil.config.email;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
@AllArgsConstructor
public class EmailConfig {

	private final EmailProperties emailProperties;

	@Bean
	public JavaMailSender getJavaMailSender() {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost("smtp.gmail.com");
		mailSender.setPort(465);

		mailSender.setUsername(emailProperties.getUsername());
		mailSender.setPassword(emailProperties.getPassword());

		Properties props = mailSender.getJavaMailProperties();
		props.put("mail.transport.protocol", "smtps");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.socketFactory.fallback", "true");
		props.put("mail.debug", "true");
		props.put("mail.smtp.ssl.enable", "true");

		return mailSender;
	}
}
