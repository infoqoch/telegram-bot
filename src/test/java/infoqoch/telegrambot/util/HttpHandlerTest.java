package infoqoch.telegrambot.util;

import infoqoch.telegrambot.bot.config.TelegramBotProperties;
import infoqoch.telegrambot.bot.response.HttpResponseWrapper;
import lombok.SneakyThrows;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
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

class HttpHandlerTest {

    // target
    HttpHandler httpHandler;

    // mock HttpClient
    HttpClient httpClient;
    HttpResponse httpResponse;
    StatusLine statusLine;
    HttpEntity httpEntity;

    @BeforeEach
    @SneakyThrows(Exception.class)
    void setUp() {
        String token = "@test_token";
        JsonBind jsonBind = DefaultJsonBind.getInstance();
        TelegramBotProperties properties = TelegramBotProperties.defaultProperties(token);

        httpClient = mock(HttpClient.class);
        httpResponse = mock(HttpResponse.class);
        statusLine = mock(StatusLine.class);
        httpEntity = mock(HttpEntity.class);

        when(httpResponse.getEntity()).thenReturn(httpEntity);
        when(httpResponse.getStatusLine()).thenReturn(statusLine);

        httpHandler = new HttpClientHttpHandler(httpClient);
    }

    @Test
    @DisplayName("200 get 정상")
    void status_200_get() throws IOException {
        // given
        // when(httpClient.execute(any(HttpPost.class))).thenReturn(httpResponse);
        when(httpClient.execute(any(HttpGet.class))).thenReturn(httpResponse);
        mockStatusCode(200);
        mockEntityBody("good");

        // when
        final HttpResponseWrapper result = httpHandler.get(new HttpGetParamMap("abc.com"));

        // then
        assertThat(result.getStatusCode()).isEqualTo(200);
        assertThat(result.getBody()).isEqualTo("good");
    }

    @Test
    @DisplayName("200 post 정상")
    void status_200_post() throws IOException {
        // given
        // when(httpClient.execute(any(HttpPost.class))).thenReturn(httpResponse);
        when(httpClient.execute(any(HttpPost.class))).thenReturn(httpResponse);
        mockStatusCode(200);
        mockEntityBody("good");

        // when
        final HttpResponseWrapper result = httpHandler.post("abc.com", "{\"name\":\"kim\"}");


        // then
        assertThat(result.getStatusCode()).isEqualTo(200);
        assertThat(result.getBody()).isEqualTo("good");
    }

    @Test
    @DisplayName("에러 대응")
    void status_400() throws IOException {
        // given
        when(httpClient.execute(any(HttpPost.class))).thenReturn(httpResponse);
        mockStatusCode(400);
        mockEntityBody("{\"ok\":false,\"error_code\":400,\"description\":\"Bad Request: chat not found\"}");

        // when
        final HttpResponseWrapper result = httpHandler.post("abc.com", "{\"name\":\"kim\"}");

        // then
        assertThat(result.getStatusCode()).isEqualTo(400);
        assertThat(result.getBody()).isEqualTo("{\"ok\":false,\"error_code\":400,\"description\":\"Bad Request: chat not found\"}");
    }

    private OngoingStubbing<Integer> mockStatusCode(int statusCode) {
        return when(statusLine.getStatusCode()).thenReturn(statusCode);
    }

    private OngoingStubbing<InputStream> mockEntityBody(String responseEntityContent) throws IOException {
        return when(httpEntity.getContent()).thenReturn(new ByteArrayInputStream(responseEntityContent.getBytes()));
    }
}