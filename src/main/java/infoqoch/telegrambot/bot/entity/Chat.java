package infoqoch.telegrambot.bot.entity;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Chat {
	private Long id;
	private String type;
	private String firstName;
	private String username;
}
