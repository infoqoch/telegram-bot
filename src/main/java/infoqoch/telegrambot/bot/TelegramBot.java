package infoqoch.telegrambot.bot;

import infoqoch.telegrambot.bot.config.TelegramBotProperties;

public interface TelegramBot {
    TelegramSend send();
    TelegramUpdate update();
    TelegramFile file();

    /* should return immutable object */
    TelegramBotProperties.Url url(); 
}
