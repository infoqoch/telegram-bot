package infoqoch.telegrambot.bot;

import infoqoch.telegrambot.bot.entity.DocumentResult;
import infoqoch.telegrambot.bot.entity.Message;
import infoqoch.telegrambot.bot.entity.Response;
import infoqoch.telegrambot.bot.request.SendDocumentRequest;
import infoqoch.telegrambot.bot.request.SendMessageRequest;
import infoqoch.telegrambot.bot.response.HttpResponseWrapper;

public interface TelegramSend {
    Response<Message> message(SendMessageRequest request);

    Response<DocumentResult> document(SendDocumentRequest request);
}
