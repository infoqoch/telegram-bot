package infoqoch.telegrambot.util;

import infoqoch.telegrambot.bot.response.HttpResponseWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class HttpClientHttpHandler implements HttpHandler{
    private final HttpClient httpClient;

    public static HttpClientHttpHandler createDefault(){
        return new HttpClientHttpHandler(HttpClients.createDefault());
    }

    @Override
    public HttpResponseWrapper get(HttpGetParamMap httpGetParamMap) {
        try{
            final HttpGet httpGet = new HttpGet(httpGetParamMap.createUrl());
            final HttpResponse response = httpClient.execute(httpGet);
            final HttpResponseWrapper wrapper = HttpResponseWrapper.of(response);
            return wrapper;

        }catch (IOException e){
            throw new IllegalStateException(e);
        }
    }

    @Override
    public HttpResponseWrapper post(String url, String json) {
        try {
            final HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(new StringEntity(json, ContentType.APPLICATION_JSON));
            final HttpResponse response = httpClient.execute(httpPost);
            final HttpResponseWrapper wrapper = HttpResponseWrapper.of(response);
            return wrapper;

        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
