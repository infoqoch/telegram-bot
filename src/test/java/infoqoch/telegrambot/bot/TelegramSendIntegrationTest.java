package infoqoch.telegrambot.bot;

import infoqoch.telegrambot.bot.config.TelegramBotProperties;
import infoqoch.telegrambot.bot.entity.Result;
import infoqoch.telegrambot.bot.entity.Response;
import infoqoch.telegrambot.bot.request.SendMessageRequest;
import infoqoch.telegrambot.util.DefaultJsonBind;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TelegramSendIntegrationTest {
    TelegramSend send = setUp();
    private TelegramBotProperties properties;
    private DefaultJsonBind jsonBind;

    private TelegramSend setUp() {

        HttpClient httpClient = HttpClients.createDefault();
        jsonBind = new DefaultJsonBind();
        properties = TelegramBotProperties.defaultProperties("telegram-token");
        return new DefaultTelegramSend(httpClient, properties, jsonBind);
    }

    @Test
    void send_message(){
        long chatId = 39327045;
        String text = "hi, 반가반가";

        final Response<Result> response = send.message(new SendMessageRequest(chatId, text));


        assertThat(response.isOk()).isTrue();

        // {"ok":true,"result":{"message_id":2092,"from":{"id":1959903402,"is_bot":true,"first_name":"coffs_test","username":"coffs_dic_test_bot"},"chat":{"id":39327045,"first_name":"\uc11d\uc9c4","type":"private"},"date":1652014357,"text":"hi, \ubc18\uac00\ubc18\uac00"}}
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