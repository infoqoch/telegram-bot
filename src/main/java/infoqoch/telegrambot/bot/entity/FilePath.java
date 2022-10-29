package infoqoch.telegrambot.bot.entity;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class FilePath {
    private String fileId;
    private String fileUniqueId;
    private long fileSize;
    private String filePath;
}
