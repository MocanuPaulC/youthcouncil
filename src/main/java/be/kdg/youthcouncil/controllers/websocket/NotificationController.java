package be.kdg.youthcouncil.controllers.websocket;

import be.kdg.youthcouncil.domain.youthcouncil.subscriptions.ActionPointSubscription;
import be.kdg.youthcouncil.service.users.UserService;
import be.kdg.youthcouncil.service.youthcouncil.modules.ActionPointService;
import be.kdg.youthcouncil.service.youthcouncil.modules.NotificationService;
import be.kdg.youthcouncil.utility.Message;
import be.kdg.youthcouncil.utility.Notification;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class NotificationController {

	private final Logger logger = LoggerFactory.getLogger(NotificationController.class);
	private final ActionPointService actionPointService;
	private final UserService userService;
	private final NotificationService notificationService;
	private final SimpMessagingTemplate simpMessagingTemplate;

	@MessageMapping ("/application")
	@SendTo ("/all/messages")
	public Message send(final Message message) throws Exception {
		return message;
	}

	@MessageMapping ("/private")
	public void sendToSpecificUser(@Payload Message message) throws InterruptedException {
		List<ActionPointSubscription> subscriptionsByActionPointId = actionPointService.findAllSubscriptionsByActionPointId(message.getEntityId());
		for (ActionPointSubscription aps : subscriptionsByActionPointId) {

			Notification notification = notificationService.create(message);
			aps.getSubscriber().addNotification(notification);
			userService.save(aps.getSubscriber());
			simpMessagingTemplate.convertAndSendToUser(aps.getSubscriber().getUsername(), "/specific", message);
		}
	}

	@PatchMapping ("/api/notifications/{userId}")
	public void markAllAsRead(@PathVariable long userId) {
		userService.markAllAsReadForUser(userId);
	}
}
