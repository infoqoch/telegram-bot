package infoqoch.telegrambot.bot.response;

import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

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

    public static HttpResponseWrapper of(int statusCode, String body) {
        log.debug("response content body : {}", body);
        return new HttpResponseWrapper(statusCode, body);
    }
}
