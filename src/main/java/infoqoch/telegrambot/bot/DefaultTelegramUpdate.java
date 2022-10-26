package infoqoch.telegrambot.bot;

import infoqoch.telegrambot.bot.config.TelegramBotProperties;
import infoqoch.telegrambot.bot.entity.Response;
import infoqoch.telegrambot.bot.entity.Update;
import infoqoch.telegrambot.bot.response.HttpResponseWrapper;
import infoqoch.telegrambot.util.HttpGetParamMap;
import infoqoch.telegrambot.util.JsonBind;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@AllArgsConstructor(access = AccessLevel.PACKAGE) @Builder
public class DefaultTelegramUpdate implements TelegramUpdate {
    private final HttpHandler httpHandler;
    private final TelegramBotProperties properties;
    private final JsonBind jsonBind;

    @Override
    public Response<List<Update>> get(long LAST_UPDATE_ID) {
        final HttpGetParamMap httpGetParamMap = new HttpGetParamMap(properties.getUrl().getGetUpdate());
        httpGetParamMap.addParam("offset", String.valueOf(LAST_UPDATE_ID + 1));
        httpGetParamMap.addParam("timeout", String.valueOf(properties.getPollingTimeOut()));
        final HttpResponseWrapper response = httpHandler.get(httpGetParamMap);
        return jsonBind.toList(response.body(), Update.class);
    }
}
