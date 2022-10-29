package infoqoch.telegrambot.bot;

import infoqoch.telegrambot.bot.config.TelegramBotProperties;
import infoqoch.telegrambot.bot.entity.Response;
import infoqoch.telegrambot.bot.request.SendDocumentRequest;
import infoqoch.telegrambot.bot.request.SendMessageRequest;
import infoqoch.telegrambot.bot.response.HttpResponseWrapper;
import infoqoch.telegrambot.bot.response.SendDocumentResponse;
import infoqoch.telegrambot.bot.response.SendMessageResponse;
import infoqoch.telegrambot.util.DefaultJsonBind;
import infoqoch.telegrambot.util.HttpHandler;
import infoqoch.telegrambot.util.JsonBind;
import infoqoch.telegrambot.util.MarkdownStringBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TelegramSendTest {
    // target
    TelegramSend send;

    // mock httpClient
    HttpHandler httpHandler;

    @BeforeEach
    void setUp() {
        String token = "@test_token";
        JsonBind jsonBind = DefaultJsonBind.getInstance();
        TelegramBotProperties properties = TelegramBotProperties.defaultProperties(token);

        httpHandler = mock(HttpHandler.class);
        send = new DefaultTelegramSend(httpHandler, properties, jsonBind);
    }

    @Test
    @DisplayName("빈 메시지 대응")
    void ex_empty_message(){
        assertThatThrownBy(()->{
            send.message(new SendMessageRequest(123123123, ""));
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("string length should be greater than 0");
    }

    @Test
    @DisplayName("200 및 정상 프로세스 대응")
    void send_message() throws IOException {
        // given
        when(httpHandler.post(any(), any())).thenReturn(HttpResponseWrapper.of(200, generateMockResponseBody("그래! 반가워", 123123123)));

        // when
        final Response<SendMessageResponse> response = send.message(new SendMessageRequest(123123123, "hi, \\ubc18\\uac00\\ubc18\\uac00"));

        // then
        assertThat(response.isOk()).isTrue();
        assertThat(response.getResult().getChat().getId()).isEqualTo(123123123);
        assertThat(response.getResult().getText()).isEqualTo("그래! 반가워");
    }

    @Test
    @DisplayName("기본적인 document 보내기")
    void send_document() throws IOException {
        // given
        String json = "{\"ok\":true,\"result\":{\"message_id\":2139,\"from\":{\"id\":1959903402,\"is_bot\":true,\"first_name\":\"coffs_test\",\"username\":\"coffs_dic_test_bot\"},\"chat\":{\"id\":123123123,\"first_name\":\"\\uc11d\\uc9c4\",\"type\":\"private\"},\"date\":1652608959,\"document\":{\"file_name\":\"sample.xlsx\",\"mime_type\":\"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet\",\"file_id\":\"BQACAgUAAxkDAAIIW2KAz78C3hv1TphEfB5ZJFQSnVslAAL_BAACg56JVdF3guuN7A6tJAQ\",\"file_unique_id\":\"AgAD_wQAAoOeiVU\",\"file_size\":26440},\"caption\":\"\\uc0d8\\ud50c \\ud30c\\uc77c\"}}";

        when(httpHandler.post(any(), any())).thenReturn(HttpResponseWrapper.of(200, json));

        // when
        final Response<SendDocumentResponse> response = send.document(new SendDocumentRequest(123123123, "BQACAgUAAxkBAAIBYWEw4E0Q63sqghpV_lzmSZ2XSCrqAAL_BAACg56JVdF3guuN7A6tIAQ", "샘플 파일"));

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
        final String json = "{\"ok\":true,\"result\":{\"message_id\":2143,\"from\":{\"id\":1959903402,\"is_bot\":true,\"first_name\":\"coffs_test\",\"username\":\"coffs_dic_test_bot\"},\"chat\":{\"id\":123123123,\"first_name\":\"\\uc11d\\uc9c4\",\"type\":\"private\"},\"date\":1652609308,\"document\":{\"file_name\":\"sample.xlsx\",\"mime_type\":\"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet\",\"file_id\":\"BQACAgUAAxkDAAIIX2KA0RyYEZNXxw7qiny1i0Jj7-RqAAL_BAACg56JVdF3guuN7A6tJAQ\",\"file_unique_id\":\"AgAD_wQAAoOeiVU\",\"file_size\":26440},\"caption\":\"\\uc774\\ud0c8\\ub9ad\\uba54\\uc2dc\\uc9c0!\",\"caption_entities\":[{\"offset\":0,\"length\":7,\"type\":\"italic\"}]}}";
        when(httpHandler.post(any(), any())).thenReturn(HttpResponseWrapper.of(200, json));

        // when
        final Response<SendDocumentResponse> response = send.document(new SendDocumentRequest(123123123, "BQACAgUAAxkBAAIBYWEw4E0Q63sqghpV_lzmSZ2XSCrqAAL_BAACg56JVdF3guuN7A6tIAQ", new MarkdownStringBuilder().italic("이탈릭메시지!")));

        // then
        assertThat(response.isOk()).isTrue();

        assertThat(response.getResult().getDocument().getMimeType()).isEqualTo("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        assertThat(response.getResult().getDocument().getFileName()).isEqualTo("sample.xlsx");
        assertThat(response.getResult().getDocument().getFileSize()).isEqualTo(26440);

        assertThat(response.getResult().getCaptionEntities().get(0).getType()).isEqualTo("italic");
    }

    @Test
    @DisplayName("400에러 대응")
    void status_code_not_found() {
        // given
        String json = "{\"ok\":false,\"error_code\":400,\"description\":\"Bad Request: can't parse entities: Can't find end of Underline entity at byte offset 4\"}";
        when(httpHandler.post(any(), any())).thenReturn(HttpResponseWrapper.of(400, json));

        // then
        final Response<SendMessageResponse> wrong_markdown = send.message(new SendMessageRequest(12354l, "wrong markdown"));
        System.out.println("wrong_markdown = " + wrong_markdown);
    }

    private String generateMockResponseBody(String text, long chatId) {
        String contentBody  = "{\"ok\":true,\"result\":{\"message_id\":2092,\"from\":{\"id\":1959903402,\"is_bot\":true,\"first_name\":\"coffs_test\",\"username\":\"coffs_dic_test_bot\"},\"chat\":{\"id\":" + chatId + ",\"first_name\":\"\\uc11d\\uc9c4\",\"type\":\"private\"},\"date\":1652014357,\"text\":\"" + text + "\"}}";
        return contentBody;
    }
}
