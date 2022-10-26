package infoqoch.telegrambot.bot;

import infoqoch.telegrambot.bot.response.HttpResponseWrapper;
import infoqoch.telegrambot.util.HttpGetParamMap;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;

import java.io.IOException;

@RequiredArgsConstructor
public class HttpClientHttpHandler implements HttpHandler{
    private final HttpClient httpClient;

    @Override
    public HttpResponseWrapper get(HttpGetParamMap httpGetParamMap) {
        try{
            final HttpGet httpGet = new HttpGet(httpGetParamMap.createUrl());
            final HttpResponse response = httpClient.execute(httpGet);
            return HttpResponseWrapper.wrap(response);
        }catch (IOException e){
            throw new IllegalStateException(e);
        }
    }

    @Override
    public HttpResponseWrapper post(String url, String json) {
        try {
            final HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(new StringEntity(json, ContentType.APPLICATION_JSON));
            return HttpResponseWrapper.wrap(httpClient.execute(httpPost));
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
