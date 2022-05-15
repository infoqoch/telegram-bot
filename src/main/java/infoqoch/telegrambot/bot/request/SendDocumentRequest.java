package infoqoch.telegrambot.bot.request;

import infoqoch.telegrambot.util.MarkdownStringBuilder;
import lombok.Getter;
import lombok.ToString;


@Getter
@ToString
public class SendDocumentRequest {
    private final long chatId;
    private final String document;
    private final String caption;
    private final String parseMode;

    public SendDocumentRequest(long chatId, String documentFileId, MarkdownStringBuilder msb) {
        this.chatId = chatId;
        this.document = documentFileId;
        this.caption = msb.text();
        this.parseMode = msb.parseMode();
    }

    public SendDocumentRequest(long chatId, String documentFileId, String beforeEscapeCaption) {
        this(chatId, documentFileId, new MarkdownStringBuilder().plain(beforeEscapeCaption));
    }
}
