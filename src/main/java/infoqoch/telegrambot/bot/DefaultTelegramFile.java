package infoqoch.telegrambot.bot;

import infoqoch.telegrambot.bot.config.TelegramBotProperties;
import infoqoch.telegrambot.bot.entity.FilePath;
import infoqoch.telegrambot.bot.entity.Response;
import infoqoch.telegrambot.bot.request.FilePathRequest;
import infoqoch.telegrambot.bot.response.HttpResponseWrapper;
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
public class DefaultTelegramFile implements TelegramFile {
    private HttpClient httpClient;
    private TelegramBotProperties properties;
    private JsonBind jsonBind;

    @Override
    public Response<FilePath> path(FilePathRequest request) {
        System.out.println("request = " + request);
        final HttpResponseWrapper response = execute(properties.getUrl().getGetFile(), jsonBind.toJson(request));
        return jsonBind.toObject(response.toJson(), FilePath.class);
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