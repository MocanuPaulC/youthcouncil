package be.kdg.youthcouncil.utility;

import lombok.Getter;

@Getter
public class Message {
	private String entityType;
	private String title;
	private String oldValue;
	private String newValue;
	private String field;
	private long entityId;

}
