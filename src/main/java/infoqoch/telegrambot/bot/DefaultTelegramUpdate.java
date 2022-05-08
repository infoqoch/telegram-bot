package infoqoch.telegrambot.bot;

import infoqoch.telegrambot.bot.config.TelegramBotProperties;
import infoqoch.telegrambot.bot.entity.Response;
import infoqoch.telegrambot.bot.entity.Update;
import infoqoch.telegrambot.bot.response.HttpResponseWrapper;
import infoqoch.telegrambot.util.JsonBind;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import java.io.IOException;
import java.util.List;

@Slf4j
@AllArgsConstructor(access = AccessLevel.PACKAGE) @Builder
public class DefaultTelegramUpdate implements TelegramUpdate {
    private HttpClient httpClient;
    private TelegramBotProperties properties;
    private JsonBind jsonBind;

    @Override
    public Response<List<Update>> get(long LAST_UPDATE_ID) {
        final HttpResponseWrapper response = execute(LAST_UPDATE_ID);
        return jsonBind.toList(response.toJson(), Update.class);
    }

    private HttpResponseWrapper execute(long LAST_UPDATE_ID) {
        try {
            return HttpResponseWrapper.wrap(httpClient.execute(generateHttpGet(LAST_UPDATE_ID)));
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private HttpGet generateHttpGet(long LAST_UPDATE_ID) {
        String url = properties.getUrl().getGetUpdate()
                        + "?offset=" + (LAST_UPDATE_ID + 1)
                        + "&timeout=" + properties.getPollingTimeOut();
        final HttpGet httpGet = new HttpGet(url);
        return httpGet;
    }
}
