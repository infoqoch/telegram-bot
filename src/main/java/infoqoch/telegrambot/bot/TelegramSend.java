package infoqoch.telegrambot.bot;

import infoqoch.telegrambot.bot.entity.Result;
import infoqoch.telegrambot.bot.entity.Response;
import infoqoch.telegrambot.bot.request.SendMessageRequest;
import org.apache.http.HttpResponse;

public interface TelegramSend {
    HttpResponse execute(String url, String contentBody);

    Response<Result> message(SendMessageRequest request);
}
