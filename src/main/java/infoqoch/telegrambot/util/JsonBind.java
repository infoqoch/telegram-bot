package infoqoch.telegrambot.util;

import infoqoch.telegrambot.bot.entity.Response;

import java.util.List;

public interface JsonBind {
    String toJson(Object object);

    <T extends Response> T toObject(String target, Class generic);

    <T extends Response> T toList(String target, Class generic);
}
