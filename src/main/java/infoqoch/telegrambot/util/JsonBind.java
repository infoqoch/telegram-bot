package infoqoch.telegrambot.util;

import infoqoch.telegrambot.bot.entity.Response;

public interface JsonBind {
    String toJson(Object object);

    <T extends Response> T toObject(String target, Class generic);
}
