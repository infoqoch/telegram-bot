package infoqoch.telegrambot.bot.request;

import infoqoch.telegrambot.util.MarkdownStringBuilder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class SendMessageRequest {
    private final long chatId;
    private final String text;
    private final String parseMode;

    public SendMessageRequest(long chatId, MarkdownStringBuilder msb) {
        this.chatId = chatId;
        this.text = msb.toString();
        this.parseMode = msb.parseMode();
    }

    public SendMessageRequest(long chatId, String beforeEscapeText) {
        this(chatId, new MarkdownStringBuilder().plain(beforeEscapeText));
    }
}
