package be.kdg.youthcouncil.service.youthcouncil.modules;

import be.kdg.youthcouncil.persistence.youthcouncil.modules.NotificationRepository;
import be.kdg.youthcouncil.utility.Message;
import be.kdg.youthcouncil.utility.Notification;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class NotificationServiceImpl implements NotificationService {

	private final NotificationRepository notificationRepository;

	@Override
	public Notification create(Message message) {
		String notificationMessage;
		if (message.getField().equals("status")) {
			notificationMessage = String.format("The %s %s has been updated from %s to %s", message.getEntityType()
			                                                                                       .toLowerCase(), message.getTitle(), message.getOldValue(), message.getNewValue());
		} else {
			notificationMessage = String.format("The %s %s has received changes to its content", message.getEntityType()
			                                                                                            .toLowerCase(), message.getTitle());
		}
		return notificationRepository.save(new Notification(notificationMessage));
	}
}
