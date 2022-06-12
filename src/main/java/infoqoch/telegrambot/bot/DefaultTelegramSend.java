package infoqoch.telegrambot.bot;

import infoqoch.telegrambot.bot.config.TelegramBotProperties;
import infoqoch.telegrambot.bot.entity.Response;
import infoqoch.telegrambot.bot.request.SendDocumentRequest;
import infoqoch.telegrambot.bot.request.SendMessageRequest;
import infoqoch.telegrambot.bot.response.HttpResponseWrapper;
import infoqoch.telegrambot.bot.response.SendDocumentResponse;
import infoqoch.telegrambot.bot.response.SendMessageResponse;
import infoqoch.telegrambot.util.JsonBind;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;

import java.io.IOException;

@Slf4j
@AllArgsConstructor(access = AccessLevel.PACKAGE) @Builder
public class DefaultTelegramSend implements TelegramSend {
    private HttpClient httpClient;
    private TelegramBotProperties properties;
    private JsonBind jsonBind;

    @Override
    public Response<SendMessageResponse> message(SendMessageRequest request) {
        final HttpResponseWrapper response = execute(properties.getUrl().getSendMessage(), jsonBind.toJson(request));
        return jsonBind.toObject(response.toJson(), SendMessageResponse.class);
    }

    @Override
    public Response<SendDocumentResponse> document(SendDocumentRequest request) {
        final HttpResponseWrapper response = execute(properties.getUrl().getSendDocument(), jsonBind.toJson(request));
        return jsonBind.toObject(response.toJson(), SendDocumentResponse.class);
    }

    HttpResponseWrapper execute(String url, String contentBody) {
        try {
            return HttpResponseWrapper.wrap(httpClient.execute(generateHttpPost(url, contentBody)));
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private HttpPost generateHttpPost(String url, String jsonString) {
        final HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(new StringEntity(jsonString, ContentType.APPLICATION_JSON));
        return httpPost;
    }
}