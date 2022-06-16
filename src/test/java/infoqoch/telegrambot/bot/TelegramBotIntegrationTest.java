package infoqoch.telegrambot.bot;

import infoqoch.telegrambot.bot.entity.FilePath;
import infoqoch.telegrambot.bot.entity.Response;
import infoqoch.telegrambot.bot.request.FilePathRequest;
import infoqoch.telegrambot.bot.request.SendMessageRequest;
import infoqoch.telegrambot.bot.response.SendMessageResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TelegramBotIntegrationTest {

    private TelegramBot bot;

    @BeforeEach
    private void setUp() {
        bot =  DefaultTelegramBotFactory.init("telegram-token");
    }

    @Test
    @DisplayName("TelegramBot.send() 테스트")
    void send_message(){
        // when
        final Response<SendMessageResponse> response = bot.send().message(new SendMessageRequest(39327045, "hi, 반가반가"));

        // then
        assertThat(response.isOk()).isTrue();
    }

    @Test
    @DisplayName("TelegramBot.file() 테스트")
    void get_file_path(){
        // when
        final Response<FilePath> response = bot.file().path(new FilePathRequest(39327045, "BQACAgUAAxkBAAIBYWEw4E0Q63sqghpV_lzmSZ2XSCrqAAL_BAACg56JVdF3guuN7A6tIAQ"));

        // then
        assertThat(response.isOk()).isTrue();
        assertThat(response.getResult().getFilePath().contains("documents"));
        assertThat(response.getResult().getFilePath().contains("xlsx"));
    }
}
