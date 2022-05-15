package infoqoch.telegrambot.bot;

import infoqoch.telegrambot.bot.entity.Message;
import infoqoch.telegrambot.bot.entity.Response;
import infoqoch.telegrambot.bot.request.SendMessageRequest;
import infoqoch.telegrambot.bot.response.HttpResponseWrapper;

public interface TelegramSend {
    // HttpResponseWrapper execute(String url, String contentBody);
    Response<Message> message(SendMessageRequest request);
}
