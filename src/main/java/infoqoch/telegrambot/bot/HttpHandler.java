package infoqoch.telegrambot.bot;

import infoqoch.telegrambot.bot.response.HttpResponseWrapper;
import infoqoch.telegrambot.util.HttpGetParamMap;

public interface HttpHandler {
    HttpResponseWrapper get(HttpGetParamMap httpGetParamMap);
    HttpResponseWrapper post(String url, String json);
}
