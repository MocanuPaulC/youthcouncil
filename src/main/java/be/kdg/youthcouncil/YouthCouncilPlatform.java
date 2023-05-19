package be.kdg.youthcouncil;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class YouthCouncilPlatform {
	public static void main(String[] args) {
		SpringApplication.run(YouthCouncilPlatform.class, args);
	}
}
