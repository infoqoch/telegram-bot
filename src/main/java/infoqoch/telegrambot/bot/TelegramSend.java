package infoqoch.telegrambot.bot;

import infoqoch.telegrambot.bot.entity.Response;
import infoqoch.telegrambot.bot.request.SendDocumentRequest;
import infoqoch.telegrambot.bot.request.SendMessageRequest;
import infoqoch.telegrambot.bot.response.SendDocumentResponse;
import infoqoch.telegrambot.bot.response.SendMessageResponse;

public interface TelegramSend {
    Response<SendMessageResponse> message(SendMessageRequest request);

    Response<SendDocumentResponse> document(SendDocumentRequest request);
}
