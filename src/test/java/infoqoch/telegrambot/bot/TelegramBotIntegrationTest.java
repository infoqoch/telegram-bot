package infoqoch.telegrambot.bot;

import infoqoch.telegrambot.bot.entity.Result;
import infoqoch.telegrambot.bot.entity.Response;
import infoqoch.telegrambot.bot.request.SendMessageRequest;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TelegramBotIntegrationTest {
    TelegramBot bot = DefaultTelegramBotFactory.init("1959903402:AAFfvMCssvDcESLewCDvj5WZk83cbnIZ08o");

    @Test
    void 간단한_메시지_보내기(){
        long chatId = 39327045;
        String text = "hi, 반가반가";

        final Response<Result> response = bot.send().message(new SendMessageRequest(chatId, text));

        System.out.println("response = " + response);

        assertThat(response.isOk()).isTrue();
    }
}
