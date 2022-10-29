package infoqoch.telegrambot.bot.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
public class TelegramUrls {
    private final String token;
    private final String sendMessage;
    private final String getUpdate;
    private final String sendDocument;
    private final String document;
    private final String base;
    private final String getFile;
    private final String file;

    static Builder builder(String token, String base) {
        return new Builder(token, base);
    }

    public static final class Builder {
        private final String token;
        private final String base;
        private String sendMessage;
        private String getUpdate;
        private String sendDocument;
        private String document;
        private String getFile;
        private String file;

        private Builder(String token, String base) {
            this.token = token;
            this.base = base;
        }

        Builder sendMessage(String sendMessage) {
            this.sendMessage =  sendMessage;
            return this;
        }

        Builder getUpdate(String getUpdate) {
            this.getUpdate =  getUpdate;
            return this;
        }

        Builder sendDocument(String sendDocument) {
            this.sendDocument =  sendDocument;
            return this;
        }

        Builder document(String document) {
            this.document =  document;
            return this;
        }

        Builder getFile(String getFile) {
            this.getFile = getFile;
            return this;
        }

        Builder file(String file) {
            this.file = token;
            return this;
        }

        TelegramUrls build() {
            return new TelegramUrls(token, sendMessage, getUpdate, sendDocument, document, base, getFile, file);
        }
    }
}


