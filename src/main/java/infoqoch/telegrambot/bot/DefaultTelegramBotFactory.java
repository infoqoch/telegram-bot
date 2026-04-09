package infoqoch.telegrambot.bot;

import infoqoch.telegrambot.bot.config.TelegramBotProperties;
import infoqoch.telegrambot.util.DefaultJsonBind;
import infoqoch.telegrambot.util.HttpClientHttpHandler;
import infoqoch.telegrambot.util.HttpHandler;
import infoqoch.telegrambot.util.JsonBind;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Executor;

@Slf4j
public class DefaultTelegramBotFactory {
    public static TelegramBot init(String token) {
        HttpHandler httpHandler = HttpClientHttpHandler.createDefault();
        JsonBind jsonBind = DefaultJsonBind.getInstance();
        TelegramBotProperties properties = TelegramBotProperties.defaultProperties(token);
        DefaultTelegramBot defaultTelegramBot = new DefaultTelegramBot(httpHandler, jsonBind, properties);
        accessTelegramToShowName(defaultTelegramBot);
        return defaultTelegramBot;
    }

    public static TelegramBot initAsync(String token, Executor executor) {
        HttpHandler httpHandler = HttpClientHttpHandler.createAsyncDefault(executor);
        JsonBind jsonBind = DefaultJsonBind.getInstance();
        TelegramBotProperties properties = TelegramBotProperties.defaultProperties(token);
        DefaultTelegramBot defaultTelegramBot = new DefaultTelegramBot(httpHandler, jsonBind, properties);
        accessTelegramToShowName(defaultTelegramBot);
        return defaultTelegramBot;
    }

    private static void accessTelegramToShowName(DefaultTelegramBot defaultTelegramBot) {
        try{
            log.info("initialized with telegram bot : {}", defaultTelegramBot.botName().path().getResult());
        }catch (Exception e){
            throw new IllegalStateException("failed to access telegram bot. check your telegram bot or others", e);
        }
    }
}
