package infoqoch.telegrambot.bot;

import infoqoch.telegrambot.bot.config.TelegramBotProperties;
import infoqoch.telegrambot.util.HttpHandler;
import infoqoch.telegrambot.util.JsonBind;

public class DefaultTelegramBot implements TelegramBot {
    private final HttpHandler httpHandler;
    private final JsonBind jsonBind;
    private final TelegramBotProperties properties;

    public DefaultTelegramBot(HttpHandler httpHandler, JsonBind jsonBind, TelegramBotProperties properties) {
        this.httpHandler = httpHandler;
        this.jsonBind = jsonBind;
        this.properties = properties;
    }

    @Override
    public TelegramUpdate update() {
        return new DefaultTelegramUpdate(httpHandler, properties, jsonBind);
    }

    @Override
    public TelegramSend send() {
        return new DefaultTelegramSend(httpHandler, properties, jsonBind);
    }

    @Override
    public TelegramFile file() {
        return new DefaultTelegramFile(httpHandler, properties, jsonBind);
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
