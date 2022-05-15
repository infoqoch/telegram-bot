package infoqoch.telegrambot.bot.entity;

import lombok.Getter;
import lombok.ToString;

import java.time.Instant;
import java.util.List;

@Getter
@ToString
public class Message {
	private Long messageId;
	private Instant date;
	private String text;
	private From from;
	private Chat chat;
	private List<Entities> entities;
}

