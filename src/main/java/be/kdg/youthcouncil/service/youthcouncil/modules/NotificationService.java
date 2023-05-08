package be.kdg.youthcouncil.service.youthcouncil.modules;

import be.kdg.youthcouncil.utility.Message;
import be.kdg.youthcouncil.utility.Notification;

public interface NotificationService {
	public Notification create(Message message);
}
