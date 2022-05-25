package infoqoch.telegrambot.bot;

import infoqoch.telegrambot.bot.config.TelegramBotProperties;
import infoqoch.telegrambot.bot.entity.Response;
import infoqoch.telegrambot.bot.entity.Update;
import infoqoch.telegrambot.util.DefaultJsonBind;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TelegramUpdateIntegrationTest {
    TelegramUpdate update;
    TelegramBotProperties properties;
    DefaultJsonBind jsonBind;

    @BeforeEach
    private void setUp() {
        HttpClient httpClient = HttpClients.createDefault();
        jsonBind = new DefaultJsonBind();
        properties = TelegramBotProperties.defaultProperties("telegram-token");
        update = new DefaultTelegramUpdate(httpClient, properties, jsonBind);
    }

    //
    @Test
    // @Disabled("update 메시지가 없으면 기다리다가 종료된다. 테스트에 유의해야 함.")
    void get_updates(){
        final Response<List<Update>> listResponse = update.get(0l);
        System.out.println("listResponse = " + listResponse);
        assertThat(listResponse.isOk()).isTrue();
        assertThat(listResponse.getResult().get(0).getUpdateId()).isGreaterThan(0l);
    }

}