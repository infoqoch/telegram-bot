package infoqoch.telegrambot.bot.entity;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Update {
	private Long updateId;
	private Message message;

	// if editedMessage is not null.... ignore this update
	private Object editedMessage;
}

