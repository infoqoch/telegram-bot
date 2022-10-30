package infoqoch.telegrambot.bot;

import infoqoch.telegrambot.bot.config.TelegramBotProperties;
import infoqoch.telegrambot.bot.entity.Response;
import infoqoch.telegrambot.bot.entity.Update;
import infoqoch.telegrambot.bot.response.HttpResponseWrapper;
import infoqoch.telegrambot.util.HttpGetParamMap;
import infoqoch.telegrambot.util.HttpHandler;
import infoqoch.telegrambot.util.JsonBind;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

@Slf4j
public class DefaultTelegramUpdate implements TelegramUpdate {
    private final HttpHandler httpHandler;
    private final TelegramBotProperties properties;
    private final JsonBind jsonBind;

    private final HttpGetParamMap httpGetParamMap;

    public DefaultTelegramUpdate(HttpHandler httpHandler, TelegramBotProperties properties, JsonBind jsonBind) {
        this.httpHandler = httpHandler;
        this.properties = properties;
        this.jsonBind = jsonBind;
        httpGetParamMap = new HttpGetParamMap(this.properties.url().getUpdate(), Map.of("timeout", String.valueOf(properties.pollingTimeOut())));
    }

    @Override
    public Response<List<Update>> get(long LAST_UPDATE_ID) {
        httpGetParamMap.addParam("offset", String.valueOf(LAST_UPDATE_ID + 1));
        
        final HttpResponseWrapper response = httpHandler.get(httpGetParamMap);
        return jsonBind.toList(response.getBody(), Update.class);
    }
}
