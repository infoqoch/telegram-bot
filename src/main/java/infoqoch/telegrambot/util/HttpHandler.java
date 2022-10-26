package infoqoch.telegrambot.util;

import infoqoch.telegrambot.bot.response.HttpResponseWrapper;

public interface HttpHandler {
    HttpResponseWrapper get(HttpGetParamMap httpGetParamMap);
    HttpResponseWrapper post(String url, String json);
}
