package infoqoch.telegrambot.util;

import infoqoch.telegrambot.bot.config.TelegramBotProperties;
import infoqoch.telegrambot.bot.response.HttpResponseWrapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class HttpHandlerTest {

    // target
    HttpHandler httpHandler;

    // mock HttpClient
    HttpClient httpClient;
    HttpResponse<String> httpResponse;

    @BeforeEach
    @SneakyThrows(Exception.class)
    void setUp() {
        String token = "@test_token";
        JsonBind jsonBind = DefaultJsonBind.getInstance();
        TelegramBotProperties properties = TelegramBotProperties.defaultProperties(token);

        httpClient = mock(HttpClient.class);
        httpResponse = mock(HttpResponse.class);

        httpHandler = new HttpClientHttpHandler(httpClient, false);
    }

    @Test
    @DisplayName("200 get 정상")
    void status_200_get() throws IOException, InterruptedException {
        // given
        when(httpClient.send(any(HttpRequest.class), eq(HttpResponse.BodyHandlers.ofString()))).thenReturn(httpResponse);
        when(httpResponse.statusCode()).thenReturn(200);
        when(httpResponse.body()).thenReturn("good");

        // when
        final HttpResponseWrapper result = httpHandler.get(new HttpGetParamMap("https://abc.com"));

        // then
        assertThat(result.getStatusCode()).isEqualTo(200);
        assertThat(result.getBody()).isEqualTo("good");
    }

    @Test
    @DisplayName("200 post 정상")
    void status_200_post() throws IOException, InterruptedException {
        // given
        when(httpClient.send(any(HttpRequest.class), eq(HttpResponse.BodyHandlers.ofString()))).thenReturn(httpResponse);
        when(httpResponse.statusCode()).thenReturn(200);
        when(httpResponse.body()).thenReturn("good");

        // when
        final HttpResponseWrapper result = httpHandler.post("https://abc.com", "{\"name\":\"kim\"}");


        // then
        assertThat(result.getStatusCode()).isEqualTo(200);
        assertThat(result.getBody()).isEqualTo("good");
    }

    @Test
    @DisplayName("에러 대응")
    void status_400() throws IOException, InterruptedException {
        // given
        when(httpClient.send(any(HttpRequest.class), eq(HttpResponse.BodyHandlers.ofString()))).thenReturn(httpResponse);
        when(httpResponse.statusCode()).thenReturn(400);
        when(httpResponse.body()).thenReturn("{\"ok\":false,\"error_code\":400,\"description\":\"Bad Request: chat not found\"}");

        // when
        final HttpResponseWrapper result = httpHandler.post("https://abc.com", "{\"name\":\"kim\"}");

        // then
        assertThat(result.getStatusCode()).isEqualTo(400);
        assertThat(result.getBody()).isEqualTo("{\"ok\":false,\"error_code\":400,\"description\":\"Bad Request: chat not found\"}");
    }
}