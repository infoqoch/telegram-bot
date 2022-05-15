package infoqoch.telegrambot.bot;

import infoqoch.telegrambot.bot.entity.Message;
import infoqoch.telegrambot.bot.entity.Response;
import infoqoch.telegrambot.bot.request.SendMessageRequest;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TelegramBotIntegrationTest {

    @Test
    void 간단한_메시지_보내기(){
        // given
        TelegramBot bot = DefaultTelegramBotFactory.init("telegram-token");

        // when
        final Response<Message> response = bot.send().message(new SendMessageRequest(39327045, "hi, 반가반가"));

        // then
        assertThat(response.isOk()).isTrue();
    }
}
