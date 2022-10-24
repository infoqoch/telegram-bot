package infoqoch.telegrambot.bot;

import infoqoch.telegrambot.IntegrationTest;
import infoqoch.telegrambot.PropertiesUtil;
import infoqoch.telegrambot.bot.config.TelegramBotProperties;
import infoqoch.telegrambot.bot.entity.FilePath;
import infoqoch.telegrambot.bot.entity.Response;
import infoqoch.telegrambot.bot.request.FilePathRequest;
import infoqoch.telegrambot.util.DefaultJsonBind;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;

import static org.assertj.core.api.Assertions.assertThat;

@EnabledIf("isIntegrationTest")
public class TelegramFileIntegrationTest extends IntegrationTest {
    TelegramFile file;
    TelegramBotProperties properties;
    DefaultJsonBind jsonBind;
    String token = PropertiesUtil.getToken("test-telegram-token");

    @BeforeEach
    private void setUp() {
        HttpClient httpClient = HttpClients.createDefault();
        jsonBind = new DefaultJsonBind();
        properties = TelegramBotProperties.defaultProperties(token);
        file = new DefaultTelegramFile(httpClient, properties, jsonBind);
    }

    @Test
    @DisplayName("file의 위치를 찾는다")
    void file_path(){
        final Response<FilePath> response = file.path(new FilePathRequest(39327045, "BQACAgUAAxkBAAIBYWEw4E0Q63sqghpV_lzmSZ2XSCrqAAL_BAACg56JVdF3guuN7A6tIAQ"));

        assertThat(response.isOk()).isTrue();
        assertThat(response.getResult().getFilePath().contains("documents"));
        assertThat(response.getResult().getFilePath().contains("xlsx"));

        // {"ok":true,"result":{"file_id":"BQACAgUAAxkDAAIIYGKA0Yg0-tSCdS0tXWJ_59cbMX-iAAL_BAACg56JVdF3guuN7A6tJAQ","file_unique_id":"AgAD_wQAAoOeiVU","file_size":26440,"file_path":"documents/file_41.xlsx"}}
    }
}
