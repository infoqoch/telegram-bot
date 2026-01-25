package infoqoch.telegrambot.util;

import infoqoch.telegrambot.bot.response.HttpResponseWrapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.Executor;

@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class HttpClientHttpHandler implements HttpHandler{
    private final HttpClient httpClient;
    private final boolean async;

    public static HttpClientHttpHandler createDefault(){
        return new HttpClientHttpHandler(HttpClient.newHttpClient(), false);
    }

    public static HttpHandler createAsyncDefault(Executor executor) {
        return new HttpClientHttpHandler(HttpClient.newBuilder().executor(executor).build(), true);
    }

    @Override
    public HttpResponseWrapper get(HttpGetParamMap httpGetParamMap) {
        try{
            URI uri = URI.create(httpGetParamMap.createUrl());
            log.trace("request url:{}", uri);
            HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();

            HttpResponse<String> response = send(request);

            log.trace("response status:{}, body:{}", response.statusCode(), response.body());
            throwExceptionWhenLessThan400(response);
            return HttpResponseWrapper.of(response.statusCode(), response.body());

        }catch (IOException | InterruptedException e){
            throw new IllegalStateException(e);
        }
    }

    @Override
    public HttpResponseWrapper post(String url, String json) {
        try {
            URI uri = URI.create(url);
            log.trace("request url:{}, body:{}", uri, json);
            HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

            HttpResponse<String> response = send(request);

            log.trace("response status:{}, body:{}", response.statusCode(), response.body());
            throwExceptionWhenLessThan400(response);
            return HttpResponseWrapper.of(response.statusCode(), response.body());

        } catch (IOException | InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }

    private HttpResponse<String> send(HttpRequest request) throws IOException, InterruptedException {
        if(async) return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString()).join();
        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private void throwExceptionWhenLessThan400(HttpResponse<String> response) {
        if(response.statusCode() >= 400)
            throw new IllegalStateException("failed to send message. status code:" + response.statusCode() + ", body:" + response.body());

    }
}
