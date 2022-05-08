package infoqoch.telegrambot.bot.config;

import lombok.*;

@Getter
@ToString
@AllArgsConstructor
public class TelegramBotProperties {
    private final Url url;
    private final int pollingTimeOut;

    public static TelegramBotProperties defaultProperties(String token) {
        String baseUrl = "https://api.telegram.org/bot" + token;
        final Url url = Url.builder()
                .base(baseUrl)
                .sendMessage(baseUrl + "/sendMessage")
                .sendDocument(baseUrl + "/sendDocument")
                .getUpdate(baseUrl + "/getUpdates")
                .document(baseUrl + "/document")
                .getFile(baseUrl + "/getFile")
                .file("https://api.telegram.org/file/bot" + token)
                .build();
        return new TelegramBotProperties(url, 600);
    }

    @Getter
    @Builder @AllArgsConstructor
    public static class Url{
        private String sendMessage;
        private String getUpdate;
        private String sendDocument;
        private String document;
        private String base;
        private String getFile;
        private String file;
    }
}
