package infoqoch.telegrambot.bot;

import infoqoch.telegrambot.bot.config.TelegramBotProperties;
import infoqoch.telegrambot.bot.entity.Message;
import infoqoch.telegrambot.bot.entity.Response;
import infoqoch.telegrambot.bot.request.SendMessageRequest;
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
import org.mockito.stubbing.OngoingStubbing;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TelegramSendTest {
    // target
    TelegramSend send;

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

        send = new DefaultTelegramSend(httpClient, properties, jsonBind);
    }

    @Test
    @DisplayName("400에러 대응")
    void status_code_not_found() throws IOException {
        // given
        mockStatusCode(400);
        mockEntityBody("{\"ok\":false,\"error_code\":400,\"description\":\"Bad Request: can't parse entities: Can't find end of Underline entity at byte offset 4\"}");

        // then
        assertThatThrownBy(()->{
            send.message(new SendMessageRequest(12354l, "wrong markdown"));
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("can't parse entities");
    }

    @Test
    @DisplayName("빈 메시지 대응")
    void ex_empty_message(){
        assertThatThrownBy(()->{
            send.message(new SendMessageRequest(39327045, ""));
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("append message greater than 0");
    }

    @Test
    @DisplayName("200 및 정상 프로세스 대응")
    void send_message() throws IOException {
        // given
        mockStatusCode(200);
        mockEntityBody(generateMockResponseBody("hi, \\ubc18\\uac00\\ubc18\\uac00", 39327045));

        // when
        final Response<Message> response = send.message(new SendMessageRequest(39327045, "hi, \\ubc18\\uac00\\ubc18\\uac00"));

        // then
        assertThat(response.isOk()).isTrue();
        assertThat(response.getResult().getChat().getId()).isEqualTo(39327045);
    }

    private String generateMockResponseBody(String text, long chatId) {
        String contentBody  = "{\"ok\":true,\"result\":{\"message_id\":2092,\"from\":{\"id\":1959903402,\"is_bot\":true,\"first_name\":\"coffs_test\",\"username\":\"coffs_dic_test_bot\"},\"chat\":{\"id\":" + chatId + ",\"first_name\":\"\\uc11d\\uc9c4\",\"type\":\"private\"},\"date\":1652014357,\"text\":\"" + text + "\"}}";
        return contentBody;
    }

    private OngoingStubbing<Integer> mockStatusCode(int statusCode) {return when(statusLine.getStatusCode()).thenReturn(statusCode);
    }

    private OngoingStubbing<InputStream> mockEntityBody(String responseEntityContent) throws IOException {
        return when(httpEntity.getContent()).thenReturn(new ByteArrayInputStream(responseEntityContent.getBytes()));
    }
}
