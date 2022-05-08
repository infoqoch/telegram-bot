package infoqoch.telegrambot.bot.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

import java.time.Instant;

@Getter
@ToString
public class Result {
	private Long messageId;
	private Instant date;
	private String text;
	private From from;
	private Chat chat;
}

