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
}
