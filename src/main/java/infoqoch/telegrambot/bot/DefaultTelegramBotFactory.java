package infoqoch.telegrambot.bot;

import infoqoch.telegrambot.bot.config.TelegramBotProperties;
import infoqoch.telegrambot.util.DefaultJsonBind;
import infoqoch.telegrambot.util.HttpClientHttpHandler;
import infoqoch.telegrambot.util.HttpHandler;
import infoqoch.telegrambot.util.JsonBind;
import org.apache.http.impl.client.HttpClients;

public class DefaultTelegramBotFactory {
    public static TelegramBot init(String token) {
        HttpHandler httpHandler = new HttpClientHttpHandler(HttpClients.createDefault());
        JsonBind jsonBind = DefaultJsonBind.getInstance();
        TelegramBotProperties properties = TelegramBotProperties.defaultProperties(token);
        return new DefaultTelegramBot(httpHandler, jsonBind, properties);
    }
}
