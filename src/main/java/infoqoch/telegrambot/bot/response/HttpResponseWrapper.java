package infoqoch.telegrambot.bot.response;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

@Slf4j
public class HttpResponseWrapper {
    // TODO
    // 해당 데이터를 필드로 두면 안된다. 필요로한 필드로 다 꺼내야 한다. 이렇게 하니까 테스트 코드 작성이 너무 어렵다.
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
        if(response.getStatusLine().getStatusCode() < 500) throw new IllegalArgumentException(body());
        throw new IllegalStateException(body());
    }

    public String body() {
        try {
            final String responseBody = EntityUtils.toString(response.getEntity());
            log.debug("response content body : {}", responseBody);
            return responseBody;
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

}
