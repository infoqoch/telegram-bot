package infoqoch.telegrambot.bot;

import infoqoch.telegrambot.bot.config.TelegramUrls;

public interface TelegramBot {
    TelegramBotName botName();
    TelegramSend send();
    TelegramUpdate update();
    TelegramFile file();

    TelegramUrls url();
}
