package be.kdg.youthcouncil.utility;

import java.util.ArrayList;
import java.util.List;

public interface Publisher {
	List<Subscriber> subscribers = new ArrayList<>();

	void notifySubscribers();

	void subscribe(Subscriber subscriber);

	void unsubscriber(Subscriber subscriber);
}
