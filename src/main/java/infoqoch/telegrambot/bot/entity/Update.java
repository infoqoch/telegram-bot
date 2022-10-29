package infoqoch.telegrambot.bot.entity;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Update {
	private Long updateId;
	private Message message;

	private Object editedMessage;
}

