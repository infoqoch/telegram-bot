package infoqoch.telegrambot.bot;

import infoqoch.telegrambot.bot.config.TelegramBotProperties;
import infoqoch.telegrambot.bot.entity.FilePath;
import infoqoch.telegrambot.bot.entity.Response;
import infoqoch.telegrambot.bot.request.FilePathRequest;
import infoqoch.telegrambot.bot.response.HttpResponseWrapper;
import infoqoch.telegrambot.util.JsonBind;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor(access = AccessLevel.PACKAGE) @Builder
public class DefaultTelegramFile implements TelegramFile {
    private HttpHandler httpHandler;
    private TelegramBotProperties properties;
    private JsonBind jsonBind;

    @Override
    public Response<FilePath> path(FilePathRequest request) {
        final HttpResponseWrapper response = httpHandler.post(properties.getUrl().getGetFile(), jsonBind.toJson(request));
        return jsonBind.toObject(response.body(), FilePath.class);
    }
}