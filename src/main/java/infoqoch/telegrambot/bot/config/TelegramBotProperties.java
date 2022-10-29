package infoqoch.telegrambot.bot.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
public class TelegramBotProperties {
    private final TelegramUrls url;
    private final int pollingTimeOut;

    public static TelegramBotProperties defaultProperties(String token) {
        String base = "https://api.telegram.org/bot" + token;
        final TelegramUrls url = TelegramUrls.builder(token, base)
                .sendMessage(base + "/sendMessage")
                .sendDocument(base + "/sendDocument")
                .getUpdate(base + "/getUpdates")
                .document(base + "/document")
                .getFile(base + "/getFile")
                .file("https://api.telegram.org/file/bot" + token)
                .build();
        return new TelegramBotProperties(url, 60);
    }
}
