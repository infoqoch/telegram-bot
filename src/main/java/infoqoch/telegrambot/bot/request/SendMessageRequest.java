package infoqoch.telegrambot.bot.request;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class SendMessageRequest {
    private final long chatId;
    private final String text;
    private final String parseMode;

    public SendMessageRequest(long chatId, String message) {
        this.chatId = chatId;
        this.text = message;
        this.parseMode = "MarkdownV2";
    }
}
