package infoqoch.telegrambot.bot;

import infoqoch.telegrambot.bot.config.TelegramBotProperties;
import infoqoch.telegrambot.util.DefaultJsonBind;
import infoqoch.telegrambot.util.HttpClientHttpHandler;
import infoqoch.telegrambot.util.HttpHandler;
import infoqoch.telegrambot.util.JsonBind;

import java.util.concurrent.Executor;

public class DefaultTelegramBotFactory {
    public static TelegramBot init(String token) {
        HttpHandler httpHandler = HttpClientHttpHandler.createDefault();
        JsonBind jsonBind = DefaultJsonBind.getInstance();
        TelegramBotProperties properties = TelegramBotProperties.defaultProperties(token);
        return new DefaultTelegramBot(httpHandler, jsonBind, properties);
    }

    public static TelegramBot initAsync(String token, Executor executor) {
        HttpHandler httpHandler = HttpClientHttpHandler.createAsyncDefault(executor);
        JsonBind jsonBind = DefaultJsonBind.getInstance();
        TelegramBotProperties properties = TelegramBotProperties.defaultProperties(token);
        return new DefaultTelegramBot(httpHandler, jsonBind, properties);
    }
}
