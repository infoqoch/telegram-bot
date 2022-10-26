package infoqoch.telegrambot.bot;

import infoqoch.telegrambot.IntegrationTest;
import infoqoch.telegrambot.PropertiesUtil;
import infoqoch.telegrambot.bot.config.TelegramBotProperties;
import infoqoch.telegrambot.bot.entity.Response;
import infoqoch.telegrambot.bot.entity.Update;
import infoqoch.telegrambot.util.DefaultJsonBind;
import infoqoch.telegrambot.util.HttpClientHttpHandler;
import infoqoch.telegrambot.util.HttpHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@EnabledIf("isIntegrationTest")
class TelegramUpdateIntegrationTest extends IntegrationTest {
    TelegramUpdate update;
    TelegramBotProperties properties;
    DefaultJsonBind jsonBind;
    String token = PropertiesUtil.getToken("test-telegram-token");

    @BeforeEach
    private void setUp() {
        HttpHandler httpHandler = HttpClientHttpHandler.createDefault();
        jsonBind = new DefaultJsonBind();
        properties = TelegramBotProperties.defaultProperties(token);
        update = new DefaultTelegramUpdate(httpHandler, properties, jsonBind);
    }

    @Test
    @Disabled("update 메시지가 없으면 기다리다가 종료된다. 테스트에 유의해야 함.")
    void get_updates(){
        final Response<List<Update>> listResponse = update.get(0l);
        System.out.println("listResponse = " + listResponse);
        assertThat(listResponse.isOk()).isTrue();
        assertThat(listResponse.getResult().get(0).getUpdateId()).isGreaterThan(0l);
    }

}