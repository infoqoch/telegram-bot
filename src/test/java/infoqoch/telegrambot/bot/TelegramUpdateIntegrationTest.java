package infoqoch.telegrambot.bot;

import infoqoch.telegrambot.PropertiesUtil;
import infoqoch.telegrambot.bot.config.TelegramBotProperties;
import infoqoch.telegrambot.bot.entity.Response;
import infoqoch.telegrambot.bot.entity.Update;
import infoqoch.telegrambot.util.DefaultJsonBind;
import infoqoch.telegrambot.util.HttpClientHttpHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@EnabledIf("infoqoch.telegrambot.IntegrationTest#isIntegrationTest")
class TelegramUpdateIntegrationTest {
    // properties
    String token = PropertiesUtil.findProperty("test.telegram.token");

    // target
    TelegramUpdate update;

    @BeforeEach
    private void setUp() {
        update = new DefaultTelegramUpdate(
                HttpClientHttpHandler.createDefault()
                , TelegramBotProperties.defaultProperties(token)
                , DefaultJsonBind.getInstance());
    }

    @Test
    @Disabled("update 메시지가 없으면 기다리다가 종료된다. 테스트를 진행하기 위해서는 채팅에 아무 메시지나 입력해야 한다.")
    void get_updates(){
        final Response<List<Update>> listResponse = update.get(0l);
        System.out.println("listResponse = " + listResponse);
        assertThat(listResponse.isOk()).isTrue();
        assertThat(listResponse.getResult().get(0).getUpdateId()).isGreaterThan(0l);
    }
}