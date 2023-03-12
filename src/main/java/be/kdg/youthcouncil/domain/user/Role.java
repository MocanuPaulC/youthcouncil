package be.kdg.youthcouncil.domain.user;

import lombok.Getter;

@Getter
public enum Role {
	GENERAL_ADMIN("ROLE_GENERAL_ADMIN"),
	COUNCIL_ADMIN("ROLE_COUNCIL_ADMIN"),
	MODERATOR("ROLE_MODERATOR"),
	MEMBER("ROLE_MEMBER");
	private final String code;

	Role(String name) {
		this.code = name;
	}
}
