package infoqoch.telegrambot.bot;

import infoqoch.telegrambot.IntegrationTest;
import infoqoch.telegrambot.PropertiesUtil;
import infoqoch.telegrambot.bot.config.TelegramBotProperties;
import infoqoch.telegrambot.bot.entity.Response;
import infoqoch.telegrambot.bot.request.SendDocumentRequest;
import infoqoch.telegrambot.bot.request.SendMessageRequest;
import infoqoch.telegrambot.bot.response.SendDocumentResponse;
import infoqoch.telegrambot.bot.response.SendMessageResponse;
import infoqoch.telegrambot.util.DefaultJsonBind;
import infoqoch.telegrambot.util.MarkdownStringBuilder;
import org.apache.http.impl.client.HttpClients;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@EnabledIf("isIntegrationTest")
class TelegramSendIntegrationTest extends IntegrationTest {
    TelegramSend send;
    TelegramBotProperties properties;
    DefaultJsonBind jsonBind;
    String token = PropertiesUtil.getToken("test-telegram-token");

    @BeforeEach
    private void setUp() {

        HttpHandler httpHandler = new HttpClientHttpHandler(HttpClients.createDefault());
        jsonBind = new DefaultJsonBind();
        properties = TelegramBotProperties.defaultProperties(token);
        send = new DefaultTelegramSend(httpHandler, properties, jsonBind);
    }

    @Test
    @DisplayName("기본적인 document 보내기")
    void send_document(){
        final Response<SendDocumentResponse> response = send.document(new SendDocumentRequest(39327045, "BQACAgUAAxkBAAIBYWEw4E0Q63sqghpV_lzmSZ2XSCrqAAL_BAACg56JVdF3guuN7A6tIAQ", "샘플 파일"));

        assertThat(response.isOk()).isTrue();
        assertThat(response.getResult().getDocument().getMimeType()).isEqualTo("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        assertThat(response.getResult().getDocument().getFileName()).isEqualTo("sample.xlsx");
        assertThat(response.getResult().getDocument().getFileSize()).isEqualTo(26440);

        // {"ok":true,"result":{"message_id":2139,"from":{"id":1959903402,"is_bot":true,"first_name":"coffs_test","username":"coffs_dic_test_bot"},"chat":{"id":39327045,"first_name":"\uc11d\uc9c4","type":"private"},"date":1652608959,"document":{"file_name":"sample.xlsx","mime_type":"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet","file_id":"BQACAgUAAxkDAAIIW2KAz78C3hv1TphEfB5ZJFQSnVslAAL_BAACg56JVdF3guuN7A6tJAQ","file_unique_id":"AgAD_wQAAoOeiVU","file_size":26440},"caption":"\uc0d8\ud50c \ud30c\uc77c"}}
    }

    @Test
    @DisplayName("document 보내기 + 마크다운")
    void send_document_markdown(){
        final Response<SendDocumentResponse> response = send.document(new SendDocumentRequest(39327045, "BQACAgUAAxkBAAIBYWEw4E0Q63sqghpV_lzmSZ2XSCrqAAL_BAACg56JVdF3guuN7A6tIAQ", new MarkdownStringBuilder().italic("이탈릭메시지!")));

        assertThat(response.isOk()).isTrue();

        assertThat(response.getResult().getDocument().getMimeType()).isEqualTo("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        assertThat(response.getResult().getDocument().getFileName()).isEqualTo("sample.xlsx");
        assertThat(response.getResult().getDocument().getFileSize()).isEqualTo(26440);

        assertThat(response.getResult().getCaptionEntities().get(0).getType()).isEqualTo("italic");

        // {"ok":true,"result":{"message_id":2143,"from":{"id":1959903402,"is_bot":true,"first_name":"coffs_test","username":"coffs_dic_test_bot"},"chat":{"id":39327045,"first_name":"\uc11d\uc9c4","type":"private"},"date":1652609308,"document":{"file_name":"sample.xlsx","mime_type":"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet","file_id":"BQACAgUAAxkDAAIIX2KA0RyYEZNXxw7qiny1i0Jj7-RqAAL_BAACg56JVdF3guuN7A6tJAQ","file_unique_id":"AgAD_wQAAoOeiVU","file_size":26440},"caption":"\uc774\ud0c8\ub9ad\uba54\uc2dc\uc9c0!","caption_entities":[{"offset":0,"length":7,"type":"italic"}]}}
    }

    @Test
    @DisplayName("기본적인 메시지 보내기")
    void send_message(){
        final Response<SendMessageResponse> response = send.message(new SendMessageRequest(39327045, "hi, 반가반가"));

        assertThat(response.isOk()).isTrue();

        // {"ok":true,"result":{"message_id":2092,"from":{"id":1959903402,"is_bot":true,"first_name":"coffs_test","username":"coffs_dic_test_bot"},"chat":{"id":39327045,"first_name":"\uc11d\uc9c4","type":"private"},"date":1652014357,"text":"hi, \ubc18\uac00\ubc18\uac00"}}
    }

    @Test
    @DisplayName("단순한 마크다운 메시지 보내기")
    void send_markdown_message(){
        // given
        MarkdownStringBuilder msb = new MarkdownStringBuilder().plain("hi!").lineSeparator().italic("italic!").lineSeparator().code("while(true) beHappy(); ");

        // when
        final Response<SendMessageResponse> response = send.message(new SendMessageRequest(39327045, msb));

        assertThat(response.isOk()).isTrue();
        assertThat(response.getResult().getText()).isEqualTo("hi!\nitalic!\nwhile(true) beHappy();");

        assertThat(response.getResult().getEntities().get(0).getType()).isEqualTo("italic");
        assertThat(response.getResult().getEntities().get(0).getOffset()).isEqualTo(4);
        assertThat(response.getResult().getEntities().get(0).getLength()).isEqualTo(7);

        assertThat(response.getResult().getEntities().get(1).getType()).isEqualTo("code");
        assertThat(response.getResult().getEntities().get(1).getOffset()).isEqualTo(12);
        assertThat(response.getResult().getEntities().get(1).getLength()).isEqualTo(22);

        // {"ok":true,"result":{"message_id":2117,"from":{"id":1959903402,"is_bot":true,"first_name":"coffs_test","username":"coffs_dic_test_bot"},"chat":{"id":39327045,"first_name":"\uc11d\uc9c4","type":"private"},"date":1652593758,"text":"hi!\nitalic!\nwhile(true) beHappy();","entities":[{"offset":4,"length":7,"type":"italic"},{"offset":12,"length":22,"type":"code"}]}}
    }

    @Test
    @DisplayName("복잡한 마크다운 메시지 보내기")
    void complex_markdown_test(){
        final MarkdownStringBuilder msb = new MarkdownStringBuilder()
                .italic("흘림글씨야").lineSeparator()
                .italic("흘림글씨야!").lineSeparator()
                .underline("언더라인*^^*이얌").lineSeparator()
                .bold("굵게가자!").lineSeparator()
                .bold("굵게##!|가자!").lineSeparator()
                .strikethrough("취소선이야ㅠ").lineSeparator()
                .code("<h3>코드블럭!</h3>").lineSeparator()
                .url("링크가자!", "https://naver.com").lineSeparator()
                .plain("<h3>코드블럭!</h3>").lineSeparator()
                .command("search", "abc 123");

        final Response<SendMessageResponse> response = send.message(new SendMessageRequest(39327045, msb));

        assertThat(response.isOk()).isTrue();
        assertThat(response.getResult().getText()).isEqualTo("흘림글씨야\n" +
                "흘림글씨야!\n" +
                "언더라인*^^*이얌\n" +
                "굵게가자!\n" +
                "굵게##!|가자!\n" +
                "취소선이야ㅠ\n" +
                "<h3>코드블럭!</h3>\n" +
                "링크가자!\n" +
                "<h3>코드블럭!</h3>\n" +
                "/search_abc_123");

        assertThat(response.getResult().getEntities().get(7).getType()).isEqualTo("text_link");
        assertThat(response.getResult().getEntities().get(7).getUrl()).isEqualTo("https://naver.com/");
        assertThat(response.getResult().getEntities().get(7).getOffset()).isEqualTo(62);
        assertThat(response.getResult().getEntities().get(7).getLength()).isEqualTo(5);

        // {"ok":true,"result":{"message_id":2123,"from":{"id":1959903402,"is_bot":true,"first_name":"coffs_test","username":"coffs_dic_test_bot"},"chat":{"id":39327045,"first_name":"\uc11d\uc9c4","type":"private"},"date":1652594295,"text":"\ud758\ub9bc\uae00\uc528\uc57c\n\ud758\ub9bc\uae00\uc528\uc57c!\n\uc5b8\ub354\ub77c\uc778*^^*\uc774\uc58c\n\uad75\uac8c\uac00\uc790!\n\uad75\uac8c##!|\uac00\uc790!\n\ucde8\uc18c\uc120\uc774\uc57c\u3160\n<h3>\ucf54\ub4dc\ube14\ub7ed!</h3>\n\ub9c1\ud06c\uac00\uc790!\n<h3>\ucf54\ub4dc\ube14\ub7ed!</h3>\n/search_abc_123","entities":[{"offset":0,"length":5,"type":"italic"},{"offset":6,"length":6,"type":"italic"},{"offset":13,"length":10,"type":"underline"},{"offset":24,"length":5,"type":"bold"},{"offset":30,"length":9,"type":"bold"},{"offset":40,"length":6,"type":"strikethrough"},{"offset":47,"length":14,"type":"code"},{"offset":62,"length":5,"type":"text_link","url":"https://naver.com/"},{"offset":83,"length":15,"type":"bot_command"}]}}
    }

    @Test
    @DisplayName("잘못된 chat_id")
    void ex_wrong_chatId(){
        assertThatThrownBy(()->{
            send.message(new SendMessageRequest(234098234092834098l, "hi, 반가반가"));
        }).isInstanceOf(IllegalArgumentException.class).hasMessageContaining("chat not found");
        // {"ok":false,"error_code":400,"description":"Bad Request: chat not found"}
    }

    @Test
    @DisplayName("잘못된 url")
    void ex_wrong_url(){
        final String wrongUrl = properties.getUrl().getSendMessage().replace("sendMessage", "weoifjweoijf");

        assertThatThrownBy(()->{
            getDefaultTelegramSend().message(new SendMessageRequest(39327045, "hi!"));
        }).isInstanceOf(IllegalArgumentException.class).hasMessageContaining("Not Found");

        // {"ok":false,"error_code":404,"description":"Not Found"}
    }

    // url의 오류를 확인하기 하여 execute를 private이 아닌 package-private으로 둔다. 그리고 실제로 통신하기 위한 send 객체를 리턴한다. exeucte는 더 나아가 interface에 정의되지 않았다.
    private DefaultTelegramSend getDefaultTelegramSend() {
        HttpHandler httpHandler = new HttpClientHttpHandler(HttpClients.createDefault());
        jsonBind = new DefaultJsonBind();
        properties = TelegramBotProperties.defaultProperties(token);

        final DefaultTelegramSend send = new DefaultTelegramSend(httpHandler, properties, jsonBind);
        return send;
    }

    @Test
    @Disabled("empty를 텔레그램 api에서 확인하는 것이 아닌, MarkdownStringBuilder 로직에서 차단함. 더는 진입할 수 없는 테스트")
    void ex_empty_message(){
        assertThatThrownBy(()->{
            getDefaultTelegramSend().message( new SendMessageRequest(39327045, ""));
        }).isInstanceOf(IllegalArgumentException.class).hasMessageContaining("message text is empty");
        // {"ok":false,"error_code":400,"description":"Bad Request: message text is empty"}
    }

    @Test
    @Disabled("escape 처리 없이 바로 메시지를 텔레그램에 보낼 수 없다.")
    void ex_send_wrong_message_with_markdown(){
        long chatId = 39327045;
        String text = "hi, __반가반가";

        assertThatThrownBy(()->{
            send.message(new SendMessageRequest(chatId, text));
        }).isInstanceOf(IllegalArgumentException.class).hasMessageContaining("can't parse entities");

        // {"ok":false,"error_code":400,"description":"Bad Request: can't parse entities: Can't find end of Underline entity at byte offset 4"}
    }
}