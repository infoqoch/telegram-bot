package infoqoch.telegrambot.bot.response;

import infoqoch.telegrambot.bot.entity.Chat;
import infoqoch.telegrambot.bot.entity.Document;
import infoqoch.telegrambot.bot.entity.Entities;
import infoqoch.telegrambot.bot.entity.From;
import lombok.Getter;
import lombok.ToString;

import java.time.Instant;
import java.util.List;

@Getter
@ToString
public class SendDocumentResponse {
    private Long messageId;
    private Instant date;
    private From from;
    private Chat chat;
    private Document document;
    private String caption;
    private List<Entities> captionEntities;
}
