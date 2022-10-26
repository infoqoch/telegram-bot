package infoqoch.telegrambot.bot.response;

import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

@Slf4j
@Getter
@ToString
public class HttpResponseWrapper {

    private final int statusCode;
    private final String body;

    private HttpResponseWrapper(int statusCode, String body) {
        this.statusCode = statusCode;
        this.body = body;
    }

    public static HttpResponseWrapper of(HttpResponse response) {
        return new HttpResponseWrapper(response.getStatusLine().getStatusCode(), extractBody(response));
    }

    public static HttpResponseWrapper of(int statusCode, String body) {
        return new HttpResponseWrapper(statusCode, body);
    }

    private static String extractBody(HttpResponse response) {
        try {
            final String responseBody = EntityUtils.toString(response.getEntity());
            log.debug("response content body : {}", responseBody);
            return responseBody;
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
