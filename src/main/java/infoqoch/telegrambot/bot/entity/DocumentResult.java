package infoqoch.telegrambot.bot.entity;

import lombok.Getter;
import lombok.ToString;

import java.time.Instant;
import java.util.List;

@Getter
@ToString
public class DocumentResult {
    private Long messageId;
    private Instant date;
    private From from;
    private Chat chat;
    private Document document;
    private String caption;
    private List<Entities> captionEntities;
}
