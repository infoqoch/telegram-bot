package infoqoch.telegrambot.bot.response;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

@Slf4j
public class HttpResponseWrapper {
    private final HttpResponse response;

    private HttpResponseWrapper(HttpResponse response) {
        this.response = response;
    }

    public static HttpResponseWrapper wrap(HttpResponse response) {
        final HttpResponseWrapper wrapper = new HttpResponseWrapper(response);
        wrapper.valid();
        return wrapper;
    }

    private void valid() {
        if(response.getStatusLine().getStatusCode() < 400) return;
        if(response.getStatusLine().getStatusCode() < 500) throw new IllegalArgumentException(toJson());
        throw new IllegalStateException(toJson());
    }

    public String toJson() {
        try {
            final String responseBody = EntityUtils.toString(response.getEntity());
            log.info("response content body : {}", responseBody);
            return responseBody;
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

}
