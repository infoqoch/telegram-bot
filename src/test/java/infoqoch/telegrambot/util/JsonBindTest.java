package infoqoch.telegrambot.util;

import infoqoch.telegrambot.bot.entity.Message;
import infoqoch.telegrambot.bot.entity.Response;
import infoqoch.telegrambot.bot.entity.Update;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class JsonBindTest {
    @Test
    void toObj_with_Wrapper() {
        String target = "{\"ok\":true,\"result\":{\"message_id\":2080,\"from\":{\"id\":1959903402,\"is_bot\":true,\"first_name\":\"coffs_test\",\"username\":\"coffs_dic_test_bot\"},\"chat\":{\"id\":39327045,\"first_name\":\"\\uc11d\\uc9c4\",\"type\":\"private\"},\"date\":1651998171,\"text\":\"hi, \\ubc18\\uac00\\ubc18\\uac00\"}}";

        final JsonBind jsonBind = new DefaultJsonBind();
        final Response<Message> response = jsonBind.toObject(target, Message.class);
        System.out.println("response = " + response);

        final Message result = response.getResult();
        System.out.println("result = " + result);

        assertThat(result.getFrom().isBot()).isTrue();
    }


    @Test
    void toJson_with_Wrapper() throws JSONException {
        String target = "{\"ok\":true,\"result\":{\"message_id\":2080,\"from\":{\"id\":1959903402,\"is_bot\":true,\"first_name\":\"coffs_test\",\"username\":\"coffs_dic_test_bot\"},\"chat\":{\"id\":39327045,\"first_name\":\"\\uc11d\\uc9c4\",\"type\":\"private\"},\"date\":1651998171,\"text\":\"hi, \\ubc18\\uac00\\ubc18\\uac00\"}}";

        final JsonBind jsonBind = new DefaultJsonBind();
        final Response<Message> result = jsonBind.toObject(target, Message.class);
        System.out.println("result = " + result);

        final String actual = jsonBind.toJson(result).replace("\"1651998171\"", "1651998171");
        System.out.println("actual = " + actual);

        JSONAssert.assertEquals(target,actual, false);
    }

    @Test
    void list_update() throws JSONException {
        String target = "{\"ok\":true,\"result\":[{\"update_id\":567841801,\n" +
                "\"message\":{\"message_id\":2095,\"from\":{\"id\":39327045,\"is_bot\":false,\"first_name\":\"\\uc11d\\uc9c4\",\"language_code\":\"ko\"},\"chat\":{\"id\":39327045,\"first_name\":\"\\uc11d\\uc9c4\",\"type\":\"private\"},\"date\":1652020474,\"text\":\"hi\"}},{\"update_id\":567841802,\n" +
                "\"message\":{\"message_id\":2100,\"from\":{\"id\":39327045,\"is_bot\":false,\"first_name\":\"\\uc11d\\uc9c4\",\"language_code\":\"ko\"},\"chat\":{\"id\":39327045,\"first_name\":\"\\uc11d\\uc9c4\",\"type\":\"private\"},\"date\":1652022395,\"text\":\"hi\"}},{\"update_id\":567841803,\n" +
                "\"message\":{\"message_id\":2101,\"from\":{\"id\":39327045,\"is_bot\":false,\"first_name\":\"\\uc11d\\uc9c4\",\"language_code\":\"ko\"},\"chat\":{\"id\":39327045,\"first_name\":\"\\uc11d\\uc9c4\",\"type\":\"private\"},\"date\":1652022396,\"text\":\"ehllo\"}}]}";

        final JsonBind jsonBind = new DefaultJsonBind();
        final Response<List<Update>> response = jsonBind.toList(target, Update.class);

        final String actual = jsonBind.toJson(response)
                .replace("\"1652022395\"", "1652022395").replace("\"1652020474\"", "1652020474").replace("\"1652022396\"", "1652022396");

        JSONAssert.assertEquals(target, actual, false);
    }

    @Test
    void empty_list() throws JSONException {
        String target = "{\"ok\":true,\"result\":[]}"; // TODO실제로 이런지 모름

        final JsonBind jsonBind = new DefaultJsonBind();
        final Response<List<Update>> response = jsonBind.toList(target, Update.class);

        assertThat(response.isOk()).isTrue();
        assertThat(response.getResult()).size().isEqualTo(0);
    }
}
