package infoqoch.telegrambot.bot;

import infoqoch.telegrambot.bot.config.TelegramBotProperties;
import infoqoch.telegrambot.bot.entity.Result;
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
    void status_code_not_found() throws IOException {
        // given
        final int statusCode = 400;
        String wrongMarkdownBody =  "{\"ok\":false,\"error_code\":400,\"description\":\"Bad Request: can't parse entities: Can't find end of Underline entity at byte offset 4\"}";

        mockStatusCode(statusCode);
        mockEntityBody(wrongMarkdownBody);

        // then
        assertThatThrownBy(()->{
            send.message(new SendMessageRequest(12354l, "wrong markdown"));
        }).isInstanceOf(IllegalArgumentException.class).hasMessageContaining("can't parse entities");
    }

    @Test
    void work_well() throws IOException {
        // given
        final int statusCode = 200;
        final String text = "hi, \\ubc18\\uac00\\ubc18\\uac00";
        final long chatId = 39327045;

        mockStatusCode(statusCode);
        mockEntityBody(generateMockResponseBody(text, chatId));

        // when
        final Response<Result> result = send.message(new SendMessageRequest(chatId, text));
        System.out.println("result = " + result);

        // then
        assertThat(result.isOk()).isTrue();
        final Result result1 = result.getResult();
        System.out.println("result1 = " + result1);

        final Long id = result.getResult().getChat().getId();
        System.out.println("id = " + id);
    }

    private String generateMockResponseBody(String text, long chatId) {
        String contentBody  = "{\"ok\":true,\"result\":{\"message_id\":2092,\"from\":{\"id\":1959903402,\"is_bot\":true,\"first_name\":\"coffs_test\",\"username\":\"coffs_dic_test_bot\"},\"chat\":{\"id\":" + chatId + ",\"first_name\":\"\\uc11d\\uc9c4\",\"type\":\"private\"},\"date\":1652014357,\"text\":\"" + text + "\"}}";
        return contentBody;
    }

    private OngoingStubbing<Integer> mockStatusCode(int statusCode) {
        return when(statusLine.getStatusCode()).thenReturn(statusCode);
    }

    private OngoingStubbing<InputStream> mockEntityBody(String responseEntitContent) throws IOException {
        return when(httpEntity.getContent()).thenReturn(new ByteArrayInputStream(responseEntitContent.getBytes()));
    }
}
