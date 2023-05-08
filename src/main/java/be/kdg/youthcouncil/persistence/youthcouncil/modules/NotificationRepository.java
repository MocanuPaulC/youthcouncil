package be.kdg.youthcouncil.persistence.youthcouncil.modules;

import be.kdg.youthcouncil.utility.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
