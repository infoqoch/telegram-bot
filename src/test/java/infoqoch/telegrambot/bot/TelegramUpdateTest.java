package infoqoch.telegrambot.bot;

import infoqoch.telegrambot.bot.config.TelegramBotProperties;
import infoqoch.telegrambot.bot.entity.Response;
import infoqoch.telegrambot.bot.entity.Update;
import infoqoch.telegrambot.util.DefaultJsonBind;
import infoqoch.telegrambot.util.JsonBind;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.stubbing.OngoingStubbing;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TelegramUpdateTest {
    // target
    TelegramUpdate update;

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
        when(httpClient.execute(any())).thenReturn(httpResponse);

        update = new DefaultTelegramUpdate(httpClient, properties, jsonBind);
    }

    @Test
    @DisplayName("1개의 updates 대응")
    void update_list() throws IOException {
        // given
        String mockResponseBody = "{\"ok\":true,\"result\":[{\"update_id\":567841804,\n" +
                "\"message\":{\"message_id\":2102,\"from\":{\"id\":39327045,\"is_bot\":false,\"first_name\":\"\\uc11d\\uc9c4\",\"language_code\":\"ko\"},\"chat\":{\"id\":39327045,\"first_name\":\"\\uc11d\\uc9c4\",\"type\":\"private\"},\"date\":1652025791,\"text\":\"hi\"}}]}";

        mockStatusCode(200);
        mockEntityBody(mockResponseBody);

        // when
        final Response<List<Update>> response = update.get(0l);
        final List<Update> updates = response.getResult();

        // then
        assertThat(response.isOk()).isTrue();
        assertThat(updates).size().isEqualTo(1);
        assertThat(updates.get(0).getUpdateId()).isEqualTo(567841804);
    }

    @Test
    @DisplayName("빈 updates")
    void update_empty() throws IOException {
        // given
        String mockResponseBody = "{\"ok\":true,\"result\":[]}";
        mockStatusCode(200);
        mockEntityBody(mockResponseBody);

        // when
        final Response<List<Update>> response = update.get(0l);
        final List<Update> updates = response.getResult();

        // then
        assertThat(response.isOk()).isTrue();
        assertThat(updates).size().isEqualTo(0);
    }

    private OngoingStubbing<Integer> mockStatusCode(int statusCode) {return when(statusLine.getStatusCode()).thenReturn(statusCode);
    }

    private OngoingStubbing<InputStream> mockEntityBody(String responseEntitContent) throws IOException {
        return when(httpEntity.getContent()).thenReturn(new ByteArrayInputStream(responseEntitContent.getBytes()));
    }
}