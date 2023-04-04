package be.kdg.youthcouncil.domain.users;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table (name = "general_admins")
public class GeneralAdmin implements Authenticable {
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	@Getter (AccessLevel.NONE)
	private long generalAdminId;
	@Getter (AccessLevel.NONE)
	@Column (unique = true)
	private String username;
	@Getter (AccessLevel.NONE)
	private String password;

	public GeneralAdmin(String username, String password) {
		this.username = username;
		this.password = password;
	}

	@Override
	public long getId() {
		return generalAdminId;
	}

	@Override
	public boolean isGA() {
		return true;
	}

	@Override
	public String getUsername() {
		return null;
	}

	@Override
	public String getPassword() {
		return null;
	}

	@Override
	public AuthenticationType getAuthenticationType() {
		return AuthenticationType.DATABASE;
	}

}
