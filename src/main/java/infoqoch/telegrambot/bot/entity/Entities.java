package infoqoch.telegrambot.bot.entity;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Entities {
	private int offset;
	private int length;
	private String type;
	private String url; // type 이 url일 경우
}
