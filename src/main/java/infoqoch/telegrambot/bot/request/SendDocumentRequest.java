package infoqoch.telegrambot.bot.request;

import infoqoch.telegrambot.util.MarkdownStringBuilder;
import lombok.Getter;
import lombok.ToString;


@Getter
@ToString
public class SendDocumentRequest {
    private final long chatId;
    /*
    * update는 file_id
    * sendMessage때 는 document
    * 로 프로퍼티의 key가 된다.
    */
    private final String document;
    private final String caption;
    private final String parseMode;

    public SendDocumentRequest(long chatId, String document, MarkdownStringBuilder msb) {
        this.chatId = chatId;
        this.document = document;
        this.caption = msb.toString();
        this.parseMode = msb.parseMode();
    }

    public SendDocumentRequest(long chatId, String document, String beforeEscapeCaption) {
        this(chatId, document, new MarkdownStringBuilder().plain(beforeEscapeCaption));
    }
}
