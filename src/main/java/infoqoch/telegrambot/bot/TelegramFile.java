package infoqoch.telegrambot.bot;

import infoqoch.telegrambot.bot.entity.FilePath;
import infoqoch.telegrambot.bot.entity.Response;
import infoqoch.telegrambot.bot.request.FilePathRequest;

public interface TelegramFile {
    Response<FilePath> path(FilePathRequest request);
}
