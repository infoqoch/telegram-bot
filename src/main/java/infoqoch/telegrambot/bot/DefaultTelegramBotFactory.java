package infoqoch.telegrambot.bot;

import infoqoch.telegrambot.bot.config.TelegramBotProperties;
import infoqoch.telegrambot.util.DefaultJsonBind;
import infoqoch.telegrambot.util.JsonBind;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;

public class DefaultTelegramBotFactory {
    public static TelegramBot init(String token) {
        HttpClient httpClient = HttpClients.createDefault();
        JsonBind jsonBind = new DefaultJsonBind();
        TelegramBotProperties properties = TelegramBotProperties.defaultProperties(token);
        return new DefaultTelegramBot(httpClient, jsonBind, properties);
    }
}
