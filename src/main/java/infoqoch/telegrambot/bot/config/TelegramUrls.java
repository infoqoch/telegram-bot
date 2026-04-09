package infoqoch.telegrambot.bot.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter @Accessors(fluent = true)
@ToString
@RequiredArgsConstructor
public class TelegramUrls {
    private final String token;
    private final String base;

    private final String botName;
    private final String sendMessage;
    private final String getUpdate;
    private final String sendDocument;
    private final String document;
    // file_id로 file_path를 찾는다.
    private final String getFile;
    // file_path로 실제 file을 추출한다.
    private final String file;

    static Builder builder(String token, String base) {
        return new Builder(token, base);
    }

    public static final class Builder {
        private final String token;
        private final String base;

        private String getMyName;
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

        Builder getMyName(String getMyName) {
            this.getMyName =  getMyName;
            return this;
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
            this.file = file;
            return this;
        }

        TelegramUrls build() {
            return new TelegramUrls(token, base, getMyName, sendMessage, getUpdate, sendDocument, document, getFile, file);
        }
    }
}


