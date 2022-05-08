package infoqoch.telegrambot.bot;

import infoqoch.telegrambot.bot.config.TelegramBotProperties;
import infoqoch.telegrambot.bot.entity.Result;
import infoqoch.telegrambot.bot.entity.Response;
import infoqoch.telegrambot.bot.request.SendMessageRequest;
import infoqoch.telegrambot.util.JsonBind;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Builder
@Slf4j
public class DefaultTelegramSend implements TelegramSend {
    private HttpClient httpClient;
    private TelegramBotProperties properties;
    private JsonBind jsonBind;

    @Override
    public Response<Result> message(SendMessageRequest request) {
        final String requestBody = jsonBind.toJson(request);
        final HttpResponse response = execute(properties.getUrl().getSendMessage(), requestBody);
        return jsonBind.toObject(responseToJson(response), Result.class);
    }

    private String responseToJson(HttpResponse response) {
        try {
            final String responseBody = EntityUtils.toString(response.getEntity());
            log.debug("response content body : {}", responseBody);
            return responseBody;
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public HttpResponse execute(String url, String contentBody) {
        try {
            final HttpResponse response = httpClient.execute(generateHttpPost(url, contentBody));
            valid(response);
            return response;
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private void valid(HttpResponse response) {
        if (response.getStatusLine().getStatusCode() < 400) return;

        if(response.getStatusLine().getStatusCode() < 500) throw new IllegalArgumentException(responseToJson(response));

        throw new IllegalStateException(responseToJson(response));
    }

    private HttpPost generateHttpPost(String url, String jsonString) {
        final HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(new StringEntity(jsonString, ContentType.APPLICATION_JSON));
        return httpPost;
    }
}