package infoqoch.telegrambot.bot;

import infoqoch.telegrambot.bot.config.TelegramBotProperties;
import infoqoch.telegrambot.bot.entity.BotName;
import infoqoch.telegrambot.bot.entity.Response;
import infoqoch.telegrambot.bot.response.HttpResponseWrapper;
import infoqoch.telegrambot.util.HttpGetParamMap;
import infoqoch.telegrambot.util.HttpHandler;
import infoqoch.telegrambot.util.JsonBind;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class DefaultBotName implements TelegramBotName {
    private final HttpHandler httpHandler;
    private final TelegramBotProperties properties;
    private final JsonBind jsonBind;

    @Override
    public Response<BotName> path() {
        final HttpResponseWrapper response = httpHandler.get(new HttpGetParamMap(properties.url().botName()));
        return jsonBind.toObject(response.getBody(), BotName.class);
    }
}