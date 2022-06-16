package infoqoch.telegrambot.bot;

import infoqoch.telegrambot.bot.config.TelegramBotProperties;
import infoqoch.telegrambot.util.JsonBind;
import org.apache.http.client.HttpClient;

public class DefaultTelegramBot implements TelegramBot {
    private final HttpClient httpClient;
    private final JsonBind jsonBind;
    private final TelegramBotProperties properties;

    public DefaultTelegramBot(HttpClient httpClient, JsonBind jsonBind, TelegramBotProperties properties) {
        this.httpClient = httpClient;
        this.jsonBind = jsonBind;
        this.properties = properties;
    }

    @Override
    public TelegramUpdate update() {
        return new DefaultTelegramUpdate(httpClient, properties, jsonBind);
    }

    @Override
    public TelegramSend send() {
        return new DefaultTelegramSend(httpClient, properties, jsonBind);
    }

    @Override
    public TelegramFile file() {
        return new DefaultTelegramFile(httpClient, properties, jsonBind);
    }

    @Override
    public TelegramBotProperties.Url url() {
        return TelegramBotProperties.Url.builder()
                .base(properties.getUrl().getBase())
                .sendMessage(properties.getUrl().getSendMessage())
                .sendDocument(properties.getUrl().getSendDocument())
                .getUpdate(properties.getUrl().getGetUpdate())
                .document(properties.getUrl().getDocument())
                .getFile(properties.getUrl().getFile())
                .file(properties.getUrl().getFile())
                .build();
    }
}
