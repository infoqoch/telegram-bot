package infoqoch.telegrambot.bot;

public interface TelegramBot {
    TelegramSend send();
    TelegramUpdate update();
    TelegramFile file();
}
