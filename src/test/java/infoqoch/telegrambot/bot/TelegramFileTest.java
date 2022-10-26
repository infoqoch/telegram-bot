package infoqoch.telegrambot.bot;

import infoqoch.telegrambot.bot.config.TelegramBotProperties;
import infoqoch.telegrambot.bot.entity.FilePath;
import infoqoch.telegrambot.bot.entity.Response;
import infoqoch.telegrambot.bot.request.FilePathRequest;
import infoqoch.telegrambot.bot.response.HttpResponseWrapper;
import infoqoch.telegrambot.util.DefaultJsonBind;
import infoqoch.telegrambot.util.JsonBind;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TelegramFileTest {
    // given
    String token = "@test_token";
    JsonBind jsonBind = new DefaultJsonBind();
    TelegramBotProperties properties = TelegramBotProperties.defaultProperties(token);
    HttpHandler httpHandler;

    // target
    TelegramFile file;

    @BeforeEach
    void setUp() throws IOException {
        httpHandler = mock(HttpHandler.class);
        file = new DefaultTelegramFile(httpHandler, properties, jsonBind);
    }

    @Test
    @DisplayName("file의 위치를 찾는다")
    void file_path() throws IOException {
        // given
        final String json = "{\"ok\":true,\"result\":{\"file_id\":\"BQACAgUAAxkDAAIIYGKA0Yg0-tSCdS0tXWJ_59cbMX-iAAL_BAACg56JVdF3guuN7A6tJAQ\",\"file_unique_id\":\"AgAD_wQAAoOeiVU\",\"file_size\":26440,\"file_path\":\"documents/file_41.xlsx\"}}";
        when(httpHandler.post(any(), any())).thenReturn(
                HttpResponseWrapper.wrap()
        );

        final Response<FilePath> response =
                file.path(new FilePathRequest(39327045, "BQACAgUAAxkBAAIBYWEw4E0Q63sqghpV_lzmSZ2XSCrqAAL_BAACg56JVdF3guuN7A6tIAQ"));

        assertThat(response.isOk()).isTrue();
        assertThat(response.getResult().getFilePath().contains("documents"));
        assertThat(response.getResult().getFilePath().contains("xlsx"));
    }

}
