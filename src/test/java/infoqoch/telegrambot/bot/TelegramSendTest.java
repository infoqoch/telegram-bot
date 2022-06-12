package infoqoch.telegrambot.bot;

import infoqoch.telegrambot.bot.config.TelegramBotProperties;
import infoqoch.telegrambot.bot.response.SendDocumentResponse;
import infoqoch.telegrambot.bot.entity.Message;
import infoqoch.telegrambot.bot.entity.Response;
import infoqoch.telegrambot.bot.request.SendDocumentRequest;
import infoqoch.telegrambot.bot.request.SendMessageRequest;
import infoqoch.telegrambot.bot.response.SendMessageResponse;
import infoqoch.telegrambot.util.DefaultJsonBind;
import infoqoch.telegrambot.util.JsonBind;
import infoqoch.telegrambot.util.MarkdownStringBuilder;
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
        final Response<SendMessageResponse> response = send.message(new SendMessageRequest(39327045, "hi, \\ubc18\\uac00\\ubc18\\uac00"));

        // then
        assertThat(response.isOk()).isTrue();
        assertThat(response.getResult().getChat().getId()).isEqualTo(39327045);
    }


    @Test
    @DisplayName("기본적인 document 보내기")
    void send_document() throws IOException {
        // given
        mockStatusCode(200);
        mockEntityBody("{\"ok\":true,\"result\":{\"message_id\":2139,\"from\":{\"id\":1959903402,\"is_bot\":true,\"first_name\":\"coffs_test\",\"username\":\"coffs_dic_test_bot\"},\"chat\":{\"id\":39327045,\"first_name\":\"\\uc11d\\uc9c4\",\"type\":\"private\"},\"date\":1652608959,\"document\":{\"file_name\":\"sample.xlsx\",\"mime_type\":\"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet\",\"file_id\":\"BQACAgUAAxkDAAIIW2KAz78C3hv1TphEfB5ZJFQSnVslAAL_BAACg56JVdF3guuN7A6tJAQ\",\"file_unique_id\":\"AgAD_wQAAoOeiVU\",\"file_size\":26440},\"caption\":\"\\uc0d8\\ud50c \\ud30c\\uc77c\"}}");

        // when
        final Response<SendDocumentResponse> response = send.document(new SendDocumentRequest(39327045, "BQACAgUAAxkBAAIBYWEw4E0Q63sqghpV_lzmSZ2XSCrqAAL_BAACg56JVdF3guuN7A6tIAQ", "샘플 파일"));

        // then
        assertThat(response.isOk()).isTrue();
        assertThat(response.getResult().getDocument().getMimeType()).isEqualTo("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        assertThat(response.getResult().getDocument().getFileName()).isEqualTo("sample.xlsx");
        assertThat(response.getResult().getDocument().getFileSize()).isEqualTo(26440);
    }

    @Test
    @DisplayName("document 보내기 + 마크다운")
    void send_document_markdown() throws IOException {
        //given
        mockStatusCode(200);
        mockEntityBody("{\"ok\":true,\"result\":{\"message_id\":2143,\"from\":{\"id\":1959903402,\"is_bot\":true,\"first_name\":\"coffs_test\",\"username\":\"coffs_dic_test_bot\"},\"chat\":{\"id\":39327045,\"first_name\":\"\\uc11d\\uc9c4\",\"type\":\"private\"},\"date\":1652609308,\"document\":{\"file_name\":\"sample.xlsx\",\"mime_type\":\"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet\",\"file_id\":\"BQACAgUAAxkDAAIIX2KA0RyYEZNXxw7qiny1i0Jj7-RqAAL_BAACg56JVdF3guuN7A6tJAQ\",\"file_unique_id\":\"AgAD_wQAAoOeiVU\",\"file_size\":26440},\"caption\":\"\\uc774\\ud0c8\\ub9ad\\uba54\\uc2dc\\uc9c0!\",\"caption_entities\":[{\"offset\":0,\"length\":7,\"type\":\"italic\"}]}}");

        // when
        final Response<SendDocumentResponse> response = send.document(new SendDocumentRequest(39327045, "BQACAgUAAxkBAAIBYWEw4E0Q63sqghpV_lzmSZ2XSCrqAAL_BAACg56JVdF3guuN7A6tIAQ", new MarkdownStringBuilder().italic("이탈릭메시지!")));

        // then
        assertThat(response.isOk()).isTrue();

        assertThat(response.getResult().getDocument().getMimeType()).isEqualTo("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        assertThat(response.getResult().getDocument().getFileName()).isEqualTo("sample.xlsx");
        assertThat(response.getResult().getDocument().getFileSize()).isEqualTo(26440);

        assertThat(response.getResult().getCaptionEntities().get(0).getType()).isEqualTo("italic");
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
