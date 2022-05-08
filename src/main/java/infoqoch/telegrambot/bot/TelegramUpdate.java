package infoqoch.telegrambot.bot;

import infoqoch.telegrambot.bot.entity.Response;
import infoqoch.telegrambot.bot.entity.Update;

import java.util.List;

public interface TelegramUpdate {
    Response<List<Update>> get(long LAST_UPDATE_ID);
}
