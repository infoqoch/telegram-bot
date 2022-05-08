package infoqoch.telegrambot.bot.entity;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class From {
    private Long id;
    private boolean isBot;
    private String firstName;
    private String username;
    private String languageCode;

}
