package infoqoch.telegrambot.bot;

import infoqoch.telegrambot.bot.config.TelegramBotProperties;
import infoqoch.telegrambot.bot.entity.FilePath;
import infoqoch.telegrambot.bot.entity.Response;
import infoqoch.telegrambot.bot.request.FilePathRequest;
import infoqoch.telegrambot.bot.response.HttpResponseWrapper;
import infoqoch.telegrambot.util.DefaultJsonBind;
import infoqoch.telegrambot.util.HttpHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TelegramFileTest {
    // mock
    HttpHandler httpHandler;

    // target
    TelegramFile file;

    @BeforeEach
    void setUp() {
        httpHandler = mock(HttpHandler.class);
        file = new DefaultTelegramFile(httpHandler, TelegramBotProperties.defaultProperties("@test_token"), DefaultJsonBind.getInstance());
    }

    @Test
    @DisplayName("file의 위치를 찾는다")
    void file_path() {
        // given
        final String json = "{\"ok\":true,\"result\":{\"file_id\":\"fileId\",\"file_unique_id\":\"AgAD_wQAAoOeiVU\",\"file_size\":26440,\"file_path\":\"documents/file_41.xlsx\"}}";

        when(httpHandler.post(any(), any())).thenReturn(HttpResponseWrapper.of(200, json));

        final Response<FilePath> response =
                file.path(new FilePathRequest(39327045, "fileId"));

        assertThat(response.isOk()).isTrue();
        assertThat(response.getResult().getFilePath().contains("documents/file_41.xlsx"));
    }

}
