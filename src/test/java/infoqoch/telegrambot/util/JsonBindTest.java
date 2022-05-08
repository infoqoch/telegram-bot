package infoqoch.telegrambot.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import infoqoch.telegrambot.bot.entity.Chat;
import infoqoch.telegrambot.bot.entity.Result;
import infoqoch.telegrambot.bot.entity.Response;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import static org.assertj.core.api.Assertions.assertThat;

public class JsonBindTest {
    @Test
    void toObj_with_Wrapper() {
        String target = "{\"ok\":true,\"result\":{\"message_id\":2080,\"from\":{\"id\":1959903402,\"is_bot\":true,\"first_name\":\"coffs_test\",\"username\":\"coffs_dic_test_bot\"},\"chat\":{\"id\":39327045,\"first_name\":\"\\uc11d\\uc9c4\",\"type\":\"private\"},\"date\":1651998171,\"text\":\"hi, \\ubc18\\uac00\\ubc18\\uac00\"}}";

        final JsonBind jsonBind = new DefaultJsonBind();
        final Response<Result> response = jsonBind.toObject(target, Result.class);
        System.out.println("response = " + response);

        final Result result = response.getResult();
        System.out.println("result = " + result);

        assertThat(result.getFrom().isBot()).isTrue();
    }


    @Test
    void toJson_with_Wrapper() throws JSONException {
        String target = "{\"ok\":true,\"result\":{\"message_id\":2080,\"from\":{\"id\":1959903402,\"is_bot\":true,\"first_name\":\"coffs_test\",\"username\":\"coffs_dic_test_bot\"},\"chat\":{\"id\":39327045,\"first_name\":\"\\uc11d\\uc9c4\",\"type\":\"private\"},\"date\":1651998171,\"text\":\"hi, \\ubc18\\uac00\\ubc18\\uac00\"}}";

        final JsonBind jsonBind = new DefaultJsonBind();
        final Response<Result> result = jsonBind.toObject(target, Result.class);
        System.out.println("result = " + result);

        final String actual = jsonBind.toJson(result).replace("\"1651998171\"", "1651998171");
        System.out.println("actual = " + actual);

        JSONAssert.assertEquals(target,actual, false);
    }
}
