package infoqoch.telegrambot.bot;

import infoqoch.telegrambot.PropertiesUtil;
import infoqoch.telegrambot.bot.entity.FilePath;
import infoqoch.telegrambot.bot.entity.Response;
import infoqoch.telegrambot.bot.request.FilePathRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;

import static org.assertj.core.api.Assertions.assertThat;

@EnabledIf("infoqoch.telegrambot.IntegrationTest#isIntegrationTest")
public class TelegramFileAndBotIntegrationTest {
    // properties
    String token = PropertiesUtil.findProperty("test.telegram.token");
    int chatId = Integer.parseInt(PropertiesUtil.findProperty("test.telegram.chat-id"));
    String fileId = PropertiesUtil.findProperty("test.telegram.document.file-id");

    // target
    TelegramBot bot = DefaultTelegramBotFactory.init(token);

    @Test
    @DisplayName("TelegramBot.file() 테스트")
    void get_file_path(){
        // when
        final Response<FilePath> response = bot.file().path(new FilePathRequest(chatId, fileId));

        // then
        assertThat(response.isOk()).isTrue();
        assertThat(response.getResult().getFilePath().contains("documents"));
    }
}