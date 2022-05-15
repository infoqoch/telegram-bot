package infoqoch.telegrambot.util;

import infoqoch.telegrambot.bot.entity.Response;

import java.util.List;

public interface JsonBind {
    String toJson(Object object);

    <T extends Response> T toObject(String target, Class generic);

    <T extends Response<List<E>>, E> T toList(String target, Class<E> generic);
}
