package infoqoch.telegrambot.bot;

import infoqoch.telegrambot.bot.config.TelegramBotProperties;
import infoqoch.telegrambot.bot.entity.FilePath;
import infoqoch.telegrambot.bot.entity.Response;
import infoqoch.telegrambot.bot.request.FilePathRequest;
import infoqoch.telegrambot.util.DefaultJsonBind;
import infoqoch.telegrambot.util.JsonBind;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.stubbing.OngoingStubbing;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TelegramFileTest {
    // target
    TelegramFile file;

    // mock httpClient
    HttpClient httpClient;
    HttpResponse httpResponse;
    StatusLine statusLine;
    HttpEntity httpEntity;

    @BeforeEach
    void setUp() throws IOException {
        String token = "@test_token";
        JsonBind jsonBind = new DefaultJsonBind();
        TelegramBotProperties properties = TelegramBotProperties.defaultProperties(token);

        httpClient = mock(HttpClient.class);
        httpResponse = mock(HttpResponse.class);
        statusLine = mock(StatusLine.class);
        httpEntity = mock(HttpEntity.class);

        when(httpResponse.getEntity()).thenReturn(httpEntity);
        when(httpResponse.getStatusLine()).thenReturn(statusLine);
        when(httpClient.execute(any(HttpPost.class))).thenReturn(httpResponse);

        file = new DefaultTelegramFile(httpClient, properties, jsonBind);
    }

    @Test
    @DisplayName("file의 위치를 찾는다")
    void file_path() throws IOException {
        // given
        mockStatusCode(200);
        mockEntityBody("{\"ok\":true,\"result\":{\"file_id\":\"BQACAgUAAxkDAAIIYGKA0Yg0-tSCdS0tXWJ_59cbMX-iAAL_BAACg56JVdF3guuN7A6tJAQ\",\"file_unique_id\":\"AgAD_wQAAoOeiVU\",\"file_size\":26440,\"file_path\":\"documents/file_41.xlsx\"}}");

        final Response<FilePath> response = file.path(new FilePathRequest(39327045, "BQACAgUAAxkBAAIBYWEw4E0Q63sqghpV_lzmSZ2XSCrqAAL_BAACg56JVdF3guuN7A6tIAQ"));

        assertThat(response.isOk()).isTrue();
        assertThat(response.getResult().getFilePath().contains("documents"));
        assertThat(response.getResult().getFilePath().contains("xlsx"));
    }
    private OngoingStubbing<Integer> mockStatusCode(int statusCode) {return when(statusLine.getStatusCode()).thenReturn(statusCode);}

    private OngoingStubbing<InputStream> mockEntityBody(String responseEntityContent) throws IOException {
        return when(httpEntity.getContent()).thenReturn(new ByteArrayInputStream(responseEntityContent.getBytes()));
    }
}
