package infoqoch.telegrambot.bot;

import infoqoch.telegrambot.bot.config.TelegramBotProperties;
import infoqoch.telegrambot.bot.entity.Response;
import infoqoch.telegrambot.bot.request.SendDocumentRequest;
import infoqoch.telegrambot.bot.request.SendMessageRequest;
import infoqoch.telegrambot.bot.response.HttpResponseWrapper;
import infoqoch.telegrambot.bot.response.SendDocumentResponse;
import infoqoch.telegrambot.bot.response.SendMessageResponse;
import infoqoch.telegrambot.util.HttpHandler;
import infoqoch.telegrambot.util.JsonBind;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class DefaultTelegramSend implements TelegramSend {
    private final HttpHandler httpHandler;
    private final TelegramBotProperties properties;
    private final JsonBind jsonBind;

    @Override
    public Response<SendMessageResponse> message(SendMessageRequest request) {
        final HttpResponseWrapper response = httpHandler.post(properties.getUrl().getSendMessage(), jsonBind.toJson(request));
        return jsonBind.toObject(response.getBody(), SendMessageResponse.class);
    }

    @Override
    public Response<SendDocumentResponse> document(SendDocumentRequest request) {
        final HttpResponseWrapper response = httpHandler.post(properties.getUrl().getSendDocument(), jsonBind.toJson(request));
        return jsonBind.toObject(response.getBody(), SendDocumentResponse.class);
    }
}