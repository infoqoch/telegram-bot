package infoqoch.telegrambot.bot;

import infoqoch.telegrambot.bot.config.TelegramBotProperties;
import infoqoch.telegrambot.bot.entity.FilePath;
import infoqoch.telegrambot.bot.entity.Response;
import infoqoch.telegrambot.bot.request.FilePathRequest;
import infoqoch.telegrambot.bot.response.HttpResponseWrapper;
import infoqoch.telegrambot.util.HttpHandler;
import infoqoch.telegrambot.util.JsonBind;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class DefaultTelegramFile implements TelegramFile {
    private final HttpHandler httpHandler;
    private final TelegramBotProperties properties;
    private final JsonBind jsonBind;

    @Override
    public Response<FilePath> path(FilePathRequest request) {
        final HttpResponseWrapper response = httpHandler.post(properties.getUrl().getGetFile(), jsonBind.toJson(request));
        return jsonBind.toObject(response.getBody(), FilePath.class);
    }
}