package infoqoch.telegrambot.bot.entity;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Document {
    private String fileName;
    private String mimeType;
    private String fileId;
    private String fileUniqueId;
    private Long fileSize;
}
