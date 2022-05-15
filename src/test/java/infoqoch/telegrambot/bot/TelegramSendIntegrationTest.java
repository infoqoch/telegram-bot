package infoqoch.telegrambot.bot;

import infoqoch.telegrambot.bot.config.TelegramBotProperties;
import infoqoch.telegrambot.bot.entity.Message;
import infoqoch.telegrambot.bot.entity.Response;
import infoqoch.telegrambot.bot.request.SendMessageRequest;
import infoqoch.telegrambot.util.DefaultJsonBind;
import infoqoch.telegrambot.util.MarkdownStringBuilder;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TelegramSendIntegrationTest {
    TelegramSend send;
    private TelegramBotProperties properties;
    private DefaultJsonBind jsonBind;

    @BeforeEach
    private void setUp() {
        HttpClient httpClient = HttpClients.createDefault();
        jsonBind = new DefaultJsonBind();
        properties = TelegramBotProperties.defaultProperties("telegram-token");
        send = new DefaultTelegramSend(httpClient, properties, jsonBind);
    }

    @Test
    void send_message(){
        long chatId = 39327045;
        String text = "hi, 반가반가";

        final Response<Message> response = send.message(new SendMessageRequest(chatId, text));


        assertThat(response.isOk()).isTrue();

        // {"ok":true,"result":{"message_id":2092,"from":{"id":1959903402,"is_bot":true,"first_name":"coffs_test","username":"coffs_dic_test_bot"},"chat":{"id":39327045,"first_name":"\uc11d\uc9c4","type":"private"},"date":1652014357,"text":"hi, \ubc18\uac00\ubc18\uac00"}}
    }

    @Test
    void send_markdown_message(){
        MarkdownStringBuilder msb = new MarkdownStringBuilder();
        msb.plain("hi!").lineSeparator().italic("italic!").lineSeparator().code("while(true) beHappy(); ");

        final Response<Message> response = send.message(new SendMessageRequest(39327045, msb.toString()));

        assertThat(response.isOk()).isTrue();

        // assertThat(response.getResult().getText()).isEqualTo("hi!"+System.lineSeparator()+"italic!"+System.lineSeparator()+"while(true) beHappy();");
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
    void test(){
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

        final Response<Message> response = send.message(new SendMessageRequest(39327045, msb.toString()));

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
    void ex_wrong_chatId(){
        long chatId = 234098234092834098l;
        String text = "hi, 반가반가";

        assertThatThrownBy(()->{
            send.message(new SendMessageRequest(chatId, text));
        }).isInstanceOf(IllegalArgumentException.class).hasMessageContaining("chat not found");

        // {"ok":false,"error_code":400,"description":"Bad Request: chat not found"}
    }

    @Test
    void ex_empty_message(){
        long chatId = 39327045;
        String text = "";

        assertThatThrownBy(()->{
            send.message(new SendMessageRequest(chatId, text));
        }).isInstanceOf(IllegalArgumentException.class).hasMessageContaining("message text is empty");

        // {"ok":false,"error_code":400,"description":"Bad Request: message text is empty"}
    }

    @Test
    void ex_wrong_url(){
        long chatId = 39327045;
        String text = "hi!";

        final String wrongUrl = properties.getUrl().getSendMessage().replace("sendMessage", "weoifjweoijf");

        assertThatThrownBy(()->{
            send.execute(wrongUrl, jsonBind.toJson(new SendMessageRequest(chatId, text)));
        }).isInstanceOf(IllegalArgumentException.class).hasMessageContaining("Not Found");

        // {"ok":false,"error_code":404,"description":"Not Found"}
    }

    @Test
    void ex_send_wrong_message_with_markdown(){
        long chatId = 39327045;
        String text = "hi, __반가반가";

        assertThatThrownBy(()->{
            send.message(new SendMessageRequest(chatId, text));
        }).isInstanceOf(IllegalArgumentException.class).hasMessageContaining("can't parse entities");

        // {"ok":false,"error_code":400,"description":"Bad Request: can't parse entities: Can't find end of Underline entity at byte offset 4"}
    }
}