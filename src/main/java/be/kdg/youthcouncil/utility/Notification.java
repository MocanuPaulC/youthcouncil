package be.kdg.youthcouncil.utility;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table (name = "notifications")
public class Notification {
	String text;
	LocalDateTime dateTime = LocalDateTime.now();
	boolean isRead = false;
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private Long notificationId;

	public Notification(String text) {
		this.text = text;

	}

}
