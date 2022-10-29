package infoqoch.telegrambot.bot;

import infoqoch.telegrambot.PropertiesUtil;
import infoqoch.telegrambot.bot.config.TelegramBotProperties;
import infoqoch.telegrambot.bot.config.TelegramUrls;
import infoqoch.telegrambot.bot.entity.Response;
import infoqoch.telegrambot.bot.request.SendDocumentRequest;
import infoqoch.telegrambot.bot.request.SendMessageRequest;
import infoqoch.telegrambot.bot.response.SendDocumentResponse;
import infoqoch.telegrambot.bot.response.SendMessageResponse;
import infoqoch.telegrambot.util.DefaultJsonBind;
import infoqoch.telegrambot.util.HttpClientHttpHandler;
import infoqoch.telegrambot.util.MarkdownStringBuilder;
import org.apache.http.impl.client.HttpClients;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@EnabledIf("infoqoch.telegrambot.IntegrationTest#isIntegrationTest")
class TelegramSendIntegrationTest {
    // properties
    String token = PropertiesUtil.findProperty("test.telegram.token");
    int chatId = Integer.parseInt(PropertiesUtil.findProperty("test.telegram.chat-id"));

    String document = PropertiesUtil.findProperty("test.telegram.document.file-id");
    String fileName = PropertiesUtil.findProperty("test.telegram.document.file-name");
    String fileType = PropertiesUtil.findProperty("test.telegram.document.file-type");
    int fileSize = Integer.parseInt(PropertiesUtil.findProperty("test.telegram.document.size"));

    // target
    TelegramSend send;

    @BeforeEach
    private void setUp() {
        send = new DefaultTelegramSend(
                new HttpClientHttpHandler(HttpClients.createDefault())
                , TelegramBotProperties.defaultProperties(token)
                , DefaultJsonBind.getInstance());
    }

    @Test
    @DisplayName("기본적인 document 보내기")
    void send_document(){
        final Response<SendDocumentResponse> response =
                send.document(new SendDocumentRequest(chatId, document, "샘플 파일"));

        assertThat(response.isOk()).isTrue();
        assertThat(response.getResult().getDocument().getMimeType()).isEqualTo(fileType);
        assertThat(response.getResult().getDocument().getFileName()).isEqualTo(fileName);
        assertThat(response.getResult().getDocument().getFileSize()).isEqualTo(fileSize);

        // {"ok":true,"result":{"message_id":2139,"from":{"id":1959903402,"is_bot":true,"first_name":"coffs_test","username":"coffs_dic_test_bot"},"chat":{"id":39327045,"first_name":"\uc11d\uc9c4","type":"private"},"date":1652608959,"document":{"file_name":"sample.xlsx","mime_type":"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet","file_id":"BQACAgUAAxkDAAIIW2KAz78C3hv1TphEfB5ZJFQSnVslAAL_BAACg56JVdF3guuN7A6tJAQ","file_unique_id":"AgAD_wQAAoOeiVU","file_size":26440},"caption":"\uc0d8\ud50c \ud30c\uc77c"}}
    }

    @Test
    @DisplayName("document 보내기 + 마크다운")
    void send_document_markdown(){
        final Response<SendDocumentResponse> response
                = send.document(new SendDocumentRequest(chatId, document, new MarkdownStringBuilder().italic("이탈릭메시지!")));

        assertThat(response.isOk()).isTrue();
        assertThat(response.getResult().getDocument().getMimeType()).isEqualTo(fileType);
        assertThat(response.getResult().getDocument().getFileName()).isEqualTo(fileName);
        assertThat(response.getResult().getDocument().getFileSize()).isEqualTo(fileSize);

        assertThat(response.getResult().getCaptionEntities().get(0).getType()).isEqualTo("italic");

        // {"ok":true,"result":{"message_id":2143,"from":{"id":1959903402,"is_bot":true,"first_name":"coffs_test","username":"coffs_dic_test_bot"},"chat":{"id":39327045,"first_name":"\uc11d\uc9c4","type":"private"},"date":1652609308,"document":{"file_name":"sample.xlsx","mime_type":"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet","file_id":"BQACAgUAAxkDAAIIX2KA0RyYEZNXxw7qiny1i0Jj7-RqAAL_BAACg56JVdF3guuN7A6tJAQ","file_unique_id":"AgAD_wQAAoOeiVU","file_size":26440},"caption":"\uc774\ud0c8\ub9ad\uba54\uc2dc\uc9c0!","caption_entities":[{"offset":0,"length":7,"type":"italic"}]}}
    }

    @Test
    @DisplayName("기본적인 메시지 보내기")
    void send_message(){
        final Response<SendMessageResponse> response = send.message(new SendMessageRequest(chatId, "hi, 반가반가"));

        assertThat(response.isOk()).isTrue();

        // {"ok":true,"result":{"message_id":2092,"from":{"id":1959903402,"is_bot":true,"first_name":"coffs_test","username":"coffs_dic_test_bot"},"chat":{"id":39327045,"first_name":"\uc11d\uc9c4","type":"private"},"date":1652014357,"text":"hi, \ubc18\uac00\ubc18\uac00"}}
    }

    @Test
    @DisplayName("단순한 마크다운 메시지 보내기")
    void send_markdown_message(){
        // given
        MarkdownStringBuilder msb = new MarkdownStringBuilder().plain("hi!").lineSeparator().italic("italic!").lineSeparator().code("while(true) beHappy(); ");

        // when
        final Response<SendMessageResponse> response = send.message(new SendMessageRequest(chatId, msb));

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

        final Response<SendMessageResponse> response = send.message(new SendMessageRequest(chatId, msb));

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
        final Response<SendMessageResponse> message = send.message(new SendMessageRequest(234098234092834098l, "hi, 반가반가"));

        assertThat(message.isOk()).isFalse();
        assertThat(message.getErrorCode()).isEqualTo(400);
        assertThat(message.getDescription()).isEqualTo("Bad Request: chat not found");

        // {"ok":false,"error_code":400,"description":"Bad Request: chat not found"}
    }

    @Test
    @Disabled("escape 처리가 되지 않았어도 그것을 무조건 esacpe 처리 함. MSB 내부에 오류가 없는 한 telegram에 잘못된 마크다운 문법을 보내지 않음.")
    void ex_send_wrong_message_with_markdown(){
        final Response<SendMessageResponse> message = send.message(new SendMessageRequest(chatId, "hi, __반가반가"));

        assertThat(message.isOk()).isFalse();
        assertThat(message.getErrorCode()).isEqualTo(400);
        assertThat(message.getDescription()).isEqualTo("Bad Request: can't parse entities: Can't find end of Underline entity at byte offset 4");

        // {"ok":false,"error_code":400,"description":"Bad Request: can't parse entities: Can't find end of Underline entity at byte offset 4"}
    }

    @Test
    @Disabled("package-private으로 변경하였음. properties의 생성을 굳이 공개할 이유가 없다고 판단")
    @DisplayName("잘못된 url")
    void ex_wrong_url(){
        final TelegramBotProperties properties = TelegramBotProperties.defaultProperties(token);

        final TelegramUrls url = null; // null이 입력 됨.
        final TelegramBotProperties wrongProp = new TelegramBotProperties(url, properties.getPollingTimeOut());

        assertThatThrownBy(()->{
            final DefaultTelegramSend send = new DefaultTelegramSend(new HttpClientHttpHandler(HttpClients.createDefault()), wrongProp, DefaultJsonBind.getInstance());
            send.message(new SendMessageRequest(chatId, "hi!"));
        }).isInstanceOf(IllegalArgumentException.class).hasMessageContaining("Not Found");
        // {"ok":false,"error_code":404,"description":"Not Found"}
    }
}