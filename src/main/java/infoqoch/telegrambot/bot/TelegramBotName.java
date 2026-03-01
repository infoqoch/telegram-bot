package infoqoch.telegrambot.bot;

import infoqoch.telegrambot.bot.entity.BotName;
import infoqoch.telegrambot.bot.entity.Response;

public interface TelegramBotName {
    Response<BotName> path();
}
