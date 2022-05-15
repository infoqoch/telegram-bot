package infoqoch.telegrambot.bot.request;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class FilePathRequest {
    private final long chatId;
    private final String fileId;

    public FilePathRequest(long chatId, String fileId) {
        this.chatId = chatId;
        this.fileId = fileId;
    }
}
